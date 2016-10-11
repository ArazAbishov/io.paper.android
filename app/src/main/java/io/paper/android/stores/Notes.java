package io.paper.android.stores;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import io.paper.android.models.Note;

public final class Notes implements Mapper<Note> {
    public static final Mapper<Note> MAPPER = new Notes();

    public static final String TABLE_NAME = "notes";
    public static final String ID = BaseColumns._ID;
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";

    private Notes() {
        // no instances
    }

    @Override
    public ContentValues toContentValues(Note note) {
        if (note == null) {
            throw new NullPointerException("note must not be null");
        }

        ContentValues contentValues = new ContentValues();

        if (!(note.id() < 0)) {
            contentValues.put(Notes.ID, note.id());
        }

        contentValues.put(Notes.TITLE, note.title());
        contentValues.put(Notes.DESCRIPTION, note.description());
        return contentValues;
    }

    @Override
    public Note toModel(Cursor cursor) {
        if (cursor == null) {
            throw new NullPointerException("Cursor must not be null");
        }

        return Note.builder()
                .id(Db.getInt(cursor, Notes.ID))
                .title(Db.getString(cursor, Notes.TITLE))
                .description(Db.getString(cursor, Notes.DESCRIPTION))
                .build();
    }
}
