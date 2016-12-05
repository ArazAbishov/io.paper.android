package io.paper.android.notes;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class NotesModule {

    @Provides
    @Singleton
    NotesRepository providesNotesRepository() {
        return new FakeNotesRepositoryImpl();
    }
}
