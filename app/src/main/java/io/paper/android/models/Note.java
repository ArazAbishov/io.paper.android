package io.paper.android.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Note {
    private final long id;
    private final String title;
    private final String description;

    public Note(String title, String description) {
        this(-1, title, description);
    }

    public Note(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        if (!(id < 0)) {
            contentValues.put(DbSchemas.Notes.ID, id);
        }

        contentValues.put(DbSchemas.Notes.TITLE, title);
        contentValues.put(DbSchemas.Notes.DESCRIPTION, description);
        return contentValues;
    }

    public static List<Note> mapNotes(Cursor cursor) {
        List<Note> notes;

        if (cursor != null && !cursor.isClosed()) {
            notes = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                notes.add(mapNote(cursor));
            }
        } else {
            notes = new ArrayList<>();
        }

        return notes;
    }

    public static Note mapNote(Cursor cursor) {
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor must not be null");
        }

        long id = Db.getInt(cursor, DbSchemas.Notes.ID);
        String title = Db.getString(cursor, DbSchemas.Notes.TITLE);
        String description = Db.getString(cursor, DbSchemas.Notes.DESCRIPTION);

        return new Note(id, title, description);
    }
}
