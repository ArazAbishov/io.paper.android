package io.paper.android.notes;

import android.content.ContentResolver;

import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class NotesModule {

    @Provides
    @Singleton
    NotesRepository providesNotesRepository(ContentResolver contentResolver,
            BriteContentResolver briteResolver) {
        return new NotesRepositoryImpl(contentResolver, briteResolver);
    }
}
