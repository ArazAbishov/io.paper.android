package io.paper.android.commons.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.paper.android.notes.Note;

public final class DbOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    private static final String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " +
            Note.TABLE_NAME + "(" +
            Note.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Note.Columns.TITLE + " TEXT," +
            Note.Columns.DESCRIPTION + " TEXT" + ")";

    private static final String DROP_NOTES_TABLE = "DROP TABLE IF EXISTS " + Note.TABLE_NAME;

    public DbOpenHelper(@NonNull Context context, @Nullable String database) {
        super(context, database, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int currentVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_NOTES_TABLE);
    }
}
