package io.paper.android.data;

import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnName;

public interface Model {

    @Nullable
    @ColumnName(BaseColumns._ID)
    Long id();
}
