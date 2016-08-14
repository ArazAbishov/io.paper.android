package io.paper.android.models;

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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static List<Note> map(Cursor cursor) {
        List<Note> notes;

        if (cursor != null && !cursor.isClosed()) {
            notes = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                long id = Db.getInt(cursor, DbSchemas.Notes.ID);
                String title = Db.getString(cursor, DbSchemas.Notes.TITLE);
                String description = Db.getString(cursor, DbSchemas.Notes.DESCRIPTION);

                notes.add(new Note(id, title, description));
            }
        } else {
            notes = new ArrayList<>();
        }

        return notes;
    }
}
