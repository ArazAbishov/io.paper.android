package io.paper.android.data.stores;

import android.app.Application;
import android.content.ContentResolver;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.paper.android.notes.Note;
import io.paper.android.notes.NotesStore;
import rx.schedulers.Schedulers;

@Module
public final class DbModule {

    @Provides
    @Singleton
    SqlBrite providesSqlBrite() {
        return SqlBrite.create();
    }

    @Provides
    @Singleton
    ContentResolver providesSqlLiteOpenHelper(Application application) {
        return application.getContentResolver();
    }

    @Provides
    @Singleton
    BriteContentResolver providesBriteContentResolver(ContentResolver resolver, SqlBrite sqlBrite) {
        BriteContentResolver briteContentResolver = sqlBrite
                .wrapContentProvider(resolver, Schedulers.io());
        briteContentResolver.setLoggingEnabled(true);
        return briteContentResolver;
    }

    @Provides
    @Singleton
    Store<Note> providesNoteStore(ContentResolver contentResolver,
            BriteContentResolver briteResolver) {
        return new NotesStore(contentResolver, briteResolver);
    }
}
