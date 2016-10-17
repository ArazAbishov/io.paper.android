package io.paper.android.data.stores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.paper.android.notes.NotesContract;

final class DbOpenHelper extends SQLiteOpenHelper {
    private static final String NAME = "Paper.db";
    private static final int VERSION = 1;

    private static final String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " +
            NotesContract.TABLE_NAME + "(" +
            NotesContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            NotesContract.COLUMN_TITLE + " TEXT," +
            NotesContract.COLUMN_DESCRIPTION + " TEXT" + ")";

    private static final String DROP_NOTES_TABLE = "DROP TABLE IF EXISTS " + NotesContract.TABLE_NAME;

    public DbOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
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
