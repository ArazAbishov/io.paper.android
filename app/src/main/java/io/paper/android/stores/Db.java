package io.paper.android.stores;

import android.database.Cursor;

final class Db {
    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;

    private Db() {
        throw new AssertionError("No instances.");
    }

    public static String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndexOrThrow(column));
    }

    public static boolean getBoolean(Cursor cursor, String column) {
        return getInt(cursor, column) == BOOLEAN_TRUE;
    }

    public static long getLong(Cursor cursor, String column) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(column));
    }

    public static int getInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(column));
    }
}
