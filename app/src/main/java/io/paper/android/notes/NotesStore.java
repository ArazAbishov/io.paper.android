package io.paper.android.notes;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteContentResolver;

import java.util.List;

import io.paper.android.data.stores.Query;
import io.paper.android.data.stores.Store;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public class NotesStore implements Store<Note> {
    private final ContentResolver contentResolver;
    private final BriteContentResolver briteContentResolver;

    public NotesStore(ContentResolver contentResolver, BriteContentResolver briteContentResolver) {
        this.contentResolver = contentResolver;
        this.briteContentResolver = briteContentResolver;
    }

    @Override
    public Observable<List<Note>> query(@NonNull Query query) {
        return briteContentResolver.createQuery(NotesContract.CONTENT_URI, query.projection(),
                query.selection(), query.selectionArgs(), query.sortOrder(), query.notifyForDescendents())
                .mapToList(new Func1<Cursor, Note>() {
                    @Override public Note call(Cursor cursor) {
                        return NotesMapper.INSTANCE.toModel(cursor);
                    }
                });
    }

    @Override
    public Observable<Long> insert(@NonNull final Note model) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override public Observable<Long> call() {
                long id = ContentUris.parseId(contentResolver.insert(NotesContract.CONTENT_URI,
                        NotesMapper.INSTANCE.toContentValues(model)));
                return Observable.just(id);
            }
        });
    }

    @Override
    public Observable<Integer> update(@NonNull final Note model) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.update(NotesContract.CONTENT_URI,
                        NotesMapper.INSTANCE.toContentValues(model), null, null));
            }
        });
    }

    @Override
    public Observable<Integer> delete(@NonNull final Note model) {
        if (model.id() == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.delete(ContentUris.withAppendedId(
                        NotesContract.CONTENT_URI, model.id()), null, null));
            }
        });
    }

    @Override
    public Observable<Integer> deleteAll() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.delete(
                        NotesContract.CONTENT_URI, null, null));
            }
        });
    }
}
