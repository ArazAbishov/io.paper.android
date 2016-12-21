package io.paper.android.data;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

@Module
public final class DbModule {

    @Nullable
    private final String databaseName;

    public DbModule(@Nullable String databaseName) {
        this.databaseName = databaseName;
    }

    @Provides
    @Singleton
    SqlBrite providesSqlBrite() {
        return new SqlBrite.Builder().build();
    }

    @Provides
    @Singleton
    SQLiteOpenHelper providesSqlLiteOpenHelper(Application application) {
        return new DbOpenHelper(application, databaseName);
    }

    @Provides
    @Singleton
    BriteDatabase providesBriteDatabase(SQLiteOpenHelper databaseOpenHelper, SqlBrite sqlBrite) {
        BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(databaseOpenHelper, Schedulers.io());
        briteDatabase.setLoggingEnabled(true);
        return briteDatabase;
    }
}
