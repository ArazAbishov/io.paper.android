package io.paper.android.listnotes;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

import io.paper.android.notes.Note;

class ListNotesDiff extends DiffUtil.Callback {
    private final List<Note> oldNotes;
    private final List<Note> newNotes;

    ListNotesDiff(@NonNull List<Note> oldNotes, @NonNull List<Note> newNotes) {
        this.oldNotes = oldNotes;
        this.newNotes = newNotes;
    }

    @Override
    public int getOldListSize() {
        return oldNotes.size();
    }

    @Override
    public int getNewListSize() {
        return newNotes.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNotes.get(oldItemPosition).id().equals(newNotes.get(newItemPosition).id());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNotes.get(oldItemPosition).equals(newNotes.get(newItemPosition));
    }
}
