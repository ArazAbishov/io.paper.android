package io.paper.android.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paper.android.R;
import io.paper.android.models.Note;

final class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private final LayoutInflater inflater;
    private final List<Note> notes;

    public NotesAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.notes = new ArrayList<>();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(inflater.inflate(R.layout.recyclerview_note, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.update(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void swap(List<Note> notes) {
        this.notes.clear();

        if (notes != null) {
            this.notes.addAll(notes);
        }

        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_note_title)
        TextView title;

        @BindView(R.id.textview_note_description)
        TextView description;

        NoteViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void update(Note note) {
            title.setText(note.title());
            description.setText(note.description());
        }
    }
}