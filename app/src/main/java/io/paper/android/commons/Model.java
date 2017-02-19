package io.paper.android.commons;

import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnName;

public interface Model {

    @Nullable
    @ColumnName(BaseColumns._ID)
    Long id();
}
