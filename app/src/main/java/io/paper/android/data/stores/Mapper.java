package io.paper.android.data.stores;

import android.content.ContentValues;
import android.database.Cursor;

import io.paper.android.data.Model;

public interface Mapper<T extends Model> {
    ContentValues toContentValues(T model);

    T toModel(Cursor cursor);
}
