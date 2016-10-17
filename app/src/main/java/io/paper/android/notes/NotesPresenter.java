package io.paper.android.notes;

import io.paper.android.ui.Presenter;

public interface NotesPresenter extends Presenter {
    void listNotes();

    void createNote();
}
