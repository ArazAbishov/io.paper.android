package io.paper.android.listnotes;

import dagger.Module;
import dagger.Provides;
import io.paper.android.commons.dagger.PerView;
import io.paper.android.commons.schedulers.SchedulerProvider;
import io.paper.android.notes.NotesRepository;

@PerView
@Module
public final class ListNotesModule {

    @Provides
    @PerView
    ListNotesPresenter notesPresenter(SchedulerProvider schedulerProvider,
            NotesRepository notesRepository) {
        return new ListNotesPresenterImpl(schedulerProvider, notesRepository);
    }
}
