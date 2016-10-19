package io.paper.android.notes;

import android.support.annotation.NonNull;
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

final class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private final LayoutInflater inflater;
    private final List<Note> notes;
    private final OnNoteClickListener onNoteClickListener;

    NotesAdapter(LayoutInflater inflater, OnNoteClickListener onNoteClickListener) {
        this.inflater = inflater;
        this.notes = new ArrayList<>();
        this.onNoteClickListener = onNoteClickListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(inflater.inflate(
                R.layout.recyclerview_item_note, parent, false), onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.update(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    void swap(List<Note> notes) {
        this.notes.clear();

        if (notes != null) {
            this.notes.addAll(notes);
        }

        notifyDataSetChanged();
    }

    interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_note_title)
        TextView title;

        @BindView(R.id.textview_note_description)
        TextView description;

        @NonNull
        OnRowClickListener onRowClickListener;

        NoteViewHolder(View itemView, OnNoteClickListener onNoteClickListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            // injecting external click listener to delegate (internal click listener)
            onRowClickListener = new OnRowClickListener(onNoteClickListener);

            // setting onClick listener to view
            itemView.setOnClickListener(onRowClickListener);
        }

        void update(Note note) {
            title.setText(note.title());
            description.setText(note.description());
            onRowClickListener.setNote(note);
        }
    }

    private static class OnRowClickListener implements View.OnClickListener {
        private final OnNoteClickListener onNoteClickListener;
        private Note note;

        OnRowClickListener(OnNoteClickListener onNoteClickListener) {
            this.onNoteClickListener = onNoteClickListener;
        }

        @Override
        public void onClick(View view) {
            onNoteClickListener.onNoteClick(note);
        }

        public void setNote(Note note) {
            this.note = note;
        }
    }
}