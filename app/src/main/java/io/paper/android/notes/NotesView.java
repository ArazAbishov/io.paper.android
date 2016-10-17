package io.paper.android.notes;

import java.util.List;

import io.paper.android.ui.View;

public interface NotesView extends View {
    void renderNotes(List<Note> notes);
}
