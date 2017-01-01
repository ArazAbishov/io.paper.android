package io.paper.android;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.paper.android.data.DbModule;
import io.paper.android.notes.NotesModule;
import io.paper.android.notes.NotesPresenter;
import io.paper.android.notes.NotesPresenterImpl;
import io.paper.android.notes.NotesRepository;
import io.paper.android.utils.SchedulerProvider;
import io.paper.android.utils.SchedulersModule;

@Module(
        includes = {
                DbModule.class, SchedulersModule.class, NotesModule.class
        }
)
class AppModule {
    private final Application application;

    AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    NotesPresenter providesNotesPresenter(SchedulerProvider schedulerProvider,
            NotesRepository notesRepository) {
        return new NotesPresenterImpl(schedulerProvider, notesRepository);
    }
}
