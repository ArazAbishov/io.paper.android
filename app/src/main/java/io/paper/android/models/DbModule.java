package io.paper.android.models;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

@Module
public final class DbModule {

    @Provides
    @Singleton
    SQLiteOpenHelper providesSqlLiteOpenHelper(Application application) {
        return new DbOpenHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite providesSqlBrite() {
        return SqlBrite.create();
    }

    @Provides
    @Singleton
    BriteDatabase providesBriteDatabase(SQLiteOpenHelper openHelper, SqlBrite sqlBrite) {
        BriteDatabase database = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
        database.setLoggingEnabled(true);
        return database;
    }
}
