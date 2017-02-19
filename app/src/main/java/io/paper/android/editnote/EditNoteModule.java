package io.paper.android.editnote;

import dagger.Module;
import dagger.Provides;
import io.paper.android.notes.NotesRepository;
import io.paper.android.commons.dagger.PerView;
import io.paper.android.commons.schedulers.SchedulerProvider;

@Module
public class EditNoteModule {
    private final Long noteId;

    public EditNoteModule(Long noteId) {
        this.noteId = noteId;
    }

    @Provides
    @PerView
    EditNotePresenter providesEditNotePresenter(
            SchedulerProvider schedulerProvider, NotesRepository notesRepository) {
        return new EditNotePresenterImpl(noteId, schedulerProvider, notesRepository);
    }
}
