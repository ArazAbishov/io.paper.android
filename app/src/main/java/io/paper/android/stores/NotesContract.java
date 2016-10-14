package io.paper.android.stores;

import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

public final class NotesContract implements Contract {
    public static final String TABLE_NAME = "notes";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(DbContract.BASE_URI, TABLE_NAME);

    // columns
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    @NonNull
    @Override
    public String table() {
        return TABLE_NAME;
    }
}
