package io.paper.android.notes;

import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class NotesModule {

    @Provides
    @Singleton
    NotesRepository providesNotesRepository(BriteDatabase briteDatabase) {
        return new NotesRepositoryImpl(briteDatabase);
    }
}
