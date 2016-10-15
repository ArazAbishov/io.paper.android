package io.paper.android.ui.views;

import java.util.List;

import io.paper.android.models.Note;

public interface NotesView extends View {
    void renderNotes(List<Note> notes);
}
