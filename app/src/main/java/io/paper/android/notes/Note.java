package io.paper.android.notes;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;

import io.paper.android.data.Model;

/**
 * Immutable model which represents Note
 */
@AutoValue
public abstract class Note implements Model {
    public static final String TABLE_NAME = "Notes";

    // columns
    public interface Columns {
        String ID = BaseColumns._ID;
        String TITLE = "title";
        String DESCRIPTION = "description";
    }

    @Nullable
    @ColumnName(Columns.TITLE)
    public abstract String title();

    @Nullable
    @ColumnName(Columns.DESCRIPTION)
    public abstract String description();

    public abstract Builder toBuilder();

    public abstract ContentValues toContentValues();

    public static Note create(Cursor cursor) {
        return AutoValue_Note.createFromCursor(cursor);
    }

    public static Builder builder() {
        return new $$AutoValue_Note.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(@Nullable Long id);

        public abstract Builder title(@Nullable String title);

        public abstract Builder description(@Nullable String description);

        public abstract Note build();
    }
}
