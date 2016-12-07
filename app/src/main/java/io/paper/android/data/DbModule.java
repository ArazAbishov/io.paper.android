package io.paper.android.data;

import android.app.Application;
import android.content.ContentResolver;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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
}
