package io.paper.android.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.Locale;

public final class DbUtils {
    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;

    private DbUtils() {
        throw new AssertionError("No instances.");
    }

    public static String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndexOrThrow(column));
    }

    public static Boolean getBoolean(Cursor cursor, String column) {
        return getInt(cursor, column) == BOOLEAN_TRUE;
    }

    public static Long getLong(Cursor cursor, String column) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(column));
    }

    public static Integer getInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(column));
    }

    @NonNull
    public static String contentTypeDir(@NonNull String resource) {
        return String.format(Locale.US, "%s/vnd.%s.%s", ContentResolver.CURSOR_DIR_BASE_TYPE,
                DbContract.AUTHORITY, resource);
    }

    @NonNull
    public static String contentTypeItem(@NonNull String resource) {
        return String.format(Locale.US, "%s/vnd.%s.%s", ContentResolver.CURSOR_ITEM_BASE_TYPE,
                DbContract.AUTHORITY, resource);
    }
}
