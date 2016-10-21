package io.paper.android.notes;

import java.util.List;

import io.paper.android.ui.View;

public interface NotesView extends View {
    void showNotes(List<Note> notes);

    void navigateToEditNoteView(Long noteId);
}
