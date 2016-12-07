package io.paper.android.notes;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import io.paper.android.data.DbContract;

public final class NotesContract {
    public static final String TABLE_NAME = "Notes";
    public static final String RESOURCE_NOTES = "notes";
    public static final String RESOURCE_NOTES_ID = RESOURCE_NOTES + "/#";

    public static Uri notes() {
        return Uri.withAppendedPath(DbContract.URI_AUTHORITY, RESOURCE_NOTES);
    }

    public static Uri notes(long id) {
        return ContentUris.withAppendedId(notes(), id);
    }

    // columns
    public interface Columns {
        String ID = BaseColumns._ID;
        String TITLE = "title";
        String DESCRIPTION = "description";
    }
}
