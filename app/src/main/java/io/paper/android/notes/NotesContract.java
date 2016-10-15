package io.paper.android.notes;

import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import io.paper.android.data.stores.Contract;
import io.paper.android.data.stores.DbContract;

public final class NotesContract implements Contract {
    public static final String TABLE_NAME = "notes";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(DbContract.BASE_URI, TABLE_NAME);

    // columns
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String[] COLUMNS = new String[]{
            COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION
    };

    @NonNull
    @Override
    public String table() {
        return TABLE_NAME;
    }
}
