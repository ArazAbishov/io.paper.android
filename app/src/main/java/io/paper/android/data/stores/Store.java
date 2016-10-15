package io.paper.android.data.stores;

import android.support.annotation.NonNull;

import java.util.List;

import io.paper.android.data.Model;
import io.paper.android.notes.Query;
import rx.Observable;

public interface Store<T extends Model> {
    Observable<List<T>> query(@NonNull Query query);

    Observable<Long> insert(@NonNull T model);

    Observable<Integer> update(@NonNull T model);

    Observable<Integer> delete(@NonNull T model);

    Observable<Integer> deleteAll();
}
