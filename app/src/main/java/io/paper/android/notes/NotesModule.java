package io.paper.android.notes;

import com.squareup.sqlbrite.BriteDatabase;

import dagger.Module;
import dagger.Provides;
import io.paper.android.commons.dagger.PerSession;

@PerSession
@Module
public class NotesModule {

    @Provides
    @PerSession
    NotesRepository notesRepository(BriteDatabase briteDatabase) {
        return new NotesRepositoryImpl(briteDatabase);
    }
}
