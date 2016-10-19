package io.paper.android.data.stores;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteContentResolver;

import java.util.List;

import io.paper.android.data.Model;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

class StoreImpl<T extends Model> implements Store<T> {
    private final ContentResolver contentResolver;
    private final BriteContentResolver briteContentResolver;
    private final Mapper<T> modelMapper;
    private final Uri modelUri;

    StoreImpl(ContentResolver contentResolver, BriteContentResolver briteContentResolver,
            Mapper<T> modelMapper, Uri modelUri) {
        this.contentResolver = contentResolver;
        this.briteContentResolver = briteContentResolver;
        this.modelMapper = modelMapper;
        this.modelUri = modelUri;
    }

    @Override
    public Observable<List<T>> query(@NonNull final Query query) {
        Uri contentUri = modelUri;
        if (query.id() != null) {
            contentUri = ContentUris.withAppendedId(contentUri, query.id());
        }

        return briteContentResolver.createQuery(contentUri, query.projection(), query.selection(),
                query.selectionArgs(), query.sortOrder(), query.notifyForDescendents())
                .mapToList(new Func1<Cursor, T>() {
                    @Override public T call(Cursor cursor) {
                        return modelMapper.toModel(cursor);
                    }
                });
    }

    @Override
    public Observable<Long> insert(@NonNull final T model) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override public Observable<Long> call() {
                long id = ContentUris.parseId(contentResolver.insert(modelUri,
                        modelMapper.toContentValues(model)));
                return Observable.just(id);
            }
        });
    }

    @Override
    public Observable<Long> insert(@NonNull final ContentValues contentValues) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override public Observable<Long> call() {
                long id = ContentUris.parseId(contentResolver.insert(modelUri, contentValues));
                return Observable.just(id);
            }
        });
    }

    @Override
    public Observable<Integer> update(@NonNull final T model) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.update(
                        ContentUris.withAppendedId(modelUri, model.id()),
                        modelMapper.toContentValues(model), null, null));
            }
        });
    }

    @Override
    public Observable<Integer> update(@NonNull final ContentValues contentValues) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.update(modelUri, contentValues, null, null));
            }
        });
    }

    @Override
    public Observable<Integer> update(@NonNull final T model, final @NonNull Where where) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.update(
                        ContentUris.withAppendedId(modelUri, model.id()),
                        modelMapper.toContentValues(model), where.where(), where.arguments()));
            }
        });
    }

    @Override
    public Observable<Integer> update(@NonNull final ContentValues contentValues,
            final @NonNull Where where) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.update(modelUri, contentValues,
                        where.where(), where.arguments()));
            }
        });
    }

    @Override
    public Observable<Integer> update(@NonNull final Long id,
            @NonNull final ContentValues contentValues) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.update(
                        ContentUris.withAppendedId(modelUri, id), contentValues, null, null));
            }
        });
    }

    @Override
    public Observable<Integer> delete() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.delete(modelUri, null, null));
            }
        });
    }

    @Override
    public Observable<Integer> delete(@NonNull final T model) {
        if (model.id() == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.delete(
                        ContentUris.withAppendedId(modelUri, model.id()), null, null));
            }
        });
    }

    @Override
    public Observable<Integer> delete(@NonNull final Where where) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.delete(
                        modelUri, where.where(), where.arguments()));
            }
        });
    }
}
