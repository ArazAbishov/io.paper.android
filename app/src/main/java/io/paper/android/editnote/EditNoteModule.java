package io.paper.android.editnote;

import dagger.Module;
import dagger.Provides;
import io.paper.android.data.stores.Store;
import io.paper.android.notes.Note;
import io.paper.android.ui.ActivityScope;

@Module
public class EditNoteModule {
    private final Long noteId;

    public EditNoteModule(Long noteId) {
        this.noteId = noteId;
    }

    @Provides
    @ActivityScope
    public EditNotePresenter providesEditNotePresenter(Store<Note> noteStore) {
        return new EditNotePresenterImpl(noteId, noteStore);
    }
}
