package io.paper.android.models;

import android.provider.BaseColumns;

interface DbSchemas {

    interface Notes {
        String TABLE_NAME = "notes";
        String ID = BaseColumns._ID;
        String TITLE = "title";
        String DESCRIPTION = "description";
    }

    String CREATE_NOTES_TABLE = "CREATE TABLE " + Notes.TABLE_NAME + "(" +
            Notes.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Notes.TITLE + " TEXT," +
            Notes.DESCRIPTION + " TEXT" + ")";

    String DROP_NOTES_TABLE = "DROP TABLE IF EXISTS " + Notes.TABLE_NAME;
}
