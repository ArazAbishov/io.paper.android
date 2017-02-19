package io.paper.android.commons.database;

import android.database.Cursor;
import android.support.annotation.NonNull;

import static com.google.common.truth.Truth.assertThat;

public final class CursorAssert {
    private final Cursor cursor;
    private int row;

    public static CursorAssert assertThatCursor(Cursor cursor) {
        return new CursorAssert(cursor);
    }

    private CursorAssert(Cursor cursor) {
        this.cursor = cursor;

        // set to first row by default
        this.row = 0;
    }

    @NonNull
    public CursorAssert hasRow(@NonNull Object... values) {
        assertThat(cursor.moveToNext()).named("row " + (row + 1) + " exists").isTrue();
        row = row + 1;

        assertThat(cursor.getColumnCount()).named("column count").isEqualTo(values.length);
        for (int index = 0; index < values.length; index++) {
            assertThat(cursor.getString(index))
                    .named("row " + row + " column '" + cursor.getColumnName(index) + "'")
                    .isEqualTo(String.valueOf(values[index]));
        }

        return this;
    }

    public void isExhausted() {
        if (cursor.moveToNext()) {
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if (i > 0) {
                    data.append(", ");
                }

                data.append(cursor.getString(i));
            }

            throw new AssertionError("Expected no more rows but was: " + data);
        }

        cursor.close();
    }
}