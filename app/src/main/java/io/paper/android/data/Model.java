package io.paper.android.data;

import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnName;

import io.paper.android.notes.NotesContract;

public interface Model {

    @Nullable
    @ColumnName(NotesContract.Columns.ID)
    Long id();
}
