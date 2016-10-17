package io.paper.android;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.paper.android.data.stores.DbModule;
import io.paper.android.data.stores.Store;
import io.paper.android.notes.Note;
import io.paper.android.notes.NotesPresenter;
import io.paper.android.notes.NotesPresenterImpl;

@Module(
        includes = {
                DbModule.class
        }
)
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    NotesPresenter providesNotesPresenter(Store<Note> noteStore) {
        return new NotesPresenterImpl(noteStore);
    }
}
