package io.paper.android.stores;

import android.content.ContentValues;
import android.database.Cursor;

import io.paper.android.models.Note;

public final class NotesMapper implements Mapper<Note> {
    public static final Mapper<Note> INSTANCE = new NotesMapper();

    @Override
    public ContentValues toContentValues(Note note) {
        if (note == null) {
            throw new NullPointerException("note must not be null");
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.COLUMN_TITLE, note.title());
        contentValues.put(NotesContract.COLUMN_DESCRIPTION, note.description());

        if (note.id() != null) {
            contentValues.put(NotesContract.COLUMN_ID, note.id());
        }

        return contentValues;
    }

    @Override
    public Note toModel(Cursor cursor) {
        if (cursor == null) {
            throw new NullPointerException("Cursor must not be null");
        }

        return Note.builder()
                .id(Db.getLong(cursor, NotesContract.COLUMN_ID))
                .title(Db.getString(cursor, NotesContract.COLUMN_TITLE))
                .description(Db.getString(cursor, NotesContract.COLUMN_DESCRIPTION))
                .build();
    }
}
