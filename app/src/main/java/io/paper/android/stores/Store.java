package io.paper.android.stores;

import java.util.List;

import io.paper.android.models.Model;

public interface Store<T extends Model> {
    long insert(T model);

    void update(T model);

    long delete(T model);

    long deleteAll();

    List<T> query();

    T query(long id);
}
