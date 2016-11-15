package io.paper.android.editnote;

import dagger.Module;
import dagger.Provides;
import io.paper.android.data.stores.Store;
import io.paper.android.notes.Note;
import io.paper.android.notes.NotesRepository;
import io.paper.android.ui.ActivityScope;
import io.paper.android.utils.SchedulerProvider;

@Module
public class EditNoteModule {
    private final Long noteId;

    public EditNoteModule(Long noteId) {
        this.noteId = noteId;
    }

    @Provides
    @ActivityScope
    public EditNotePresenter providesEditNotePresenter(SchedulerProvider schedulerProvider,
                                                       NotesRepository notesRepository) {
        return new EditNotePresenterImpl(noteId, schedulerProvider, notesRepository);
    }
}
