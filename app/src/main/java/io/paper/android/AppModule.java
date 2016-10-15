package io.paper.android;

import android.app.Application;

import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.paper.android.stores.DbModule;
import io.paper.android.ui.presenters.NotesPresenter;
import io.paper.android.ui.presenters.NotesPresenterImpl;

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
    NotesPresenter providesNotesPresenter(BriteContentResolver briteContentResolver) {
        return new NotesPresenterImpl(briteContentResolver);
    }
}
