package io.paper.android.listnotes;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paper.android.R;
import io.paper.android.notes.Note;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import rx.exceptions.OnErrorNotImplementedException;

final class ListNotesAdapter extends RecyclerView.Adapter<ListNotesAdapter.NoteViewHolder> {
    private final LayoutInflater inflater;
    private final List<Note> notes;
    private final FlowableProcessor<ListNotesAction> subject;

    ListNotesAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.notes = new ArrayList<>();
        this.subject = PublishProcessor.create();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(parent, inflater.inflate(
                R.layout.recyclerview_item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.update(notes.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @NonNull
    public Flowable<ListNotesAction> asFlowable() {
        return subject;
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).id();
    }

    void swap(@NonNull List<Note> updates) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new ListNotesDiff(notes, updates));

        notes.clear();
        notes.addAll(updates);

        diffResult.dispatchUpdatesTo(this);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_note_title)
        TextView title;

        @BindView(R.id.textview_note_description)
        TextView description;

        Note note;

        @SuppressWarnings("CheckReturnValue")
        NoteViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            subscribe(parent, itemView);
        }

        final Disposable subscribe(ViewGroup parent, View itemView) {
            return RxView.clicks(itemView)
                    .takeUntil(RxView.detaches(parent))
                    .map(click -> ListNotesAction.click(note))
                    .subscribe(action -> subject.onNext(action), throwable -> {
                        throw new OnErrorNotImplementedException(throwable);
                    });
        }

        final void update(Note updated) {
            note = updated;
            title.setText(note.title());
            description.setText(note.description());
        }
    }
}