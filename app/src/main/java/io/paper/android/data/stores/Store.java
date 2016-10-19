package io.paper.android.data.stores;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.paper.android.data.Model;
import rx.Observable;

public interface Store<T extends Model> {
    Observable<List<T>> query(@NonNull Query query);

    // insert methods

    Observable<Long> insert(@NonNull T model);

    Observable<Long> insert(@NonNull ContentValues contentValues);

    // update methods

    Observable<Integer> update(@NonNull T model);

    Observable<Integer> update(@NonNull ContentValues contentValues);

    Observable<Integer> update(@NonNull T model, @Nullable Where where);

    Observable<Integer> update(@NonNull ContentValues contentValues, @Nullable Where where);

    Observable<Integer> update(@NonNull Long id, @NonNull ContentValues contentValues);

    // delete methods

    Observable<Integer> delete();

    Observable<Integer> delete(@NonNull T model);

    Observable<Integer> delete(@NonNull Where where);
}
