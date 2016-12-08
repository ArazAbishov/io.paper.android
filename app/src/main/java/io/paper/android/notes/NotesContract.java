package io.paper.android.notes;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import io.paper.android.data.DbContract;
import io.paper.android.data.DbUtils;

public final class NotesContract {
    public static final String TABLE_NAME = "Notes";
    public static final String RESOURCE_NOTES = "notes";
    public static final String RESOURCE_NOTES_ID = RESOURCE_NOTES + "/#";

    public static final String CONTENT_TYPE_DIR = DbUtils.contentTypeDir(RESOURCE_NOTES);
    public static final String CONTENT_TYPE_ITEM = DbUtils.contentTypeItem(RESOURCE_NOTES);

    public static Uri notes() {
        return Uri.withAppendedPath(DbContract.AUTHORITY_URI, RESOURCE_NOTES);
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
