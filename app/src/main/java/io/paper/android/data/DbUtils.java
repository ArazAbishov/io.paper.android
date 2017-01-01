package io.paper.android.data;

import android.database.Cursor;

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
}
