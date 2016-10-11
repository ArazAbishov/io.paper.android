package io.paper.android.stores;

import android.content.ContentValues;
import android.database.Cursor;

import io.paper.android.models.Model;

public interface Mapper<T extends Model> {
    ContentValues toContentValues(T model);

    T toModel(Cursor cursor);
}
