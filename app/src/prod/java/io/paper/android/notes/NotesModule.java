package io.paper.android.notes;

import android.content.ContentResolver;

import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.paper.android.data.stores.Store;
import io.paper.android.data.stores.StoreImpl;

@Module
public final class NotesModule {

    @Provides
    @Singleton
    Store<Note> providesNoteStore(ContentResolver contentResolver,
                                  BriteContentResolver briteResolver) {
        return new StoreImpl<>(contentResolver, briteResolver,
                NotesMapper.INSTANCE, NotesContract.CONTENT_URI);
    }

    @Provides
    @Singleton
    NotesRepository providesNotesRepository(Store<Note> noteStore) {
        return new NotesRepositoryImpl(noteStore);
    }
}
