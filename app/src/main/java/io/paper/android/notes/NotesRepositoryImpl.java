package io.paper.android.notes;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.squareup.sqlbrite.BriteContentResolver;

import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

class NotesRepositoryImpl implements NotesRepository {
    private final ContentResolver contentResolver;
    private final BriteContentResolver briteContentResolver;

    NotesRepositoryImpl(ContentResolver contentResolver, BriteContentResolver briteContentResolver) {
        this.contentResolver = contentResolver;
        this.briteContentResolver = briteContentResolver;
    }

    @Override
    public Observable<Long> add(final Note note) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override public Observable<Long> call() {
                Uri noteUri = contentResolver.insert(NotesContract.notes(),
                        note.toContentValues());
                return Observable.just(ContentUris.parseId(noteUri));
            }
        });
    }

    @Override
    public Observable<Integer> putTitle(final Long noteId, final String title) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(NotesContract.Columns.TITLE, title);

                return Observable.just(contentResolver.update(
                        NotesContract.notes(noteId), contentValues, null, null));
            }
        });
    }

    @Override
    public Observable<Integer> putDescription(final Long noteId, final String description) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(NotesContract.Columns.DESCRIPTION, description);

                return Observable.just(contentResolver.update(NotesContract.notes(noteId),
                        contentValues, null, null));
            }
        });
    }

    @Override
    public Observable<List<Note>> list() {
        return briteContentResolver.createQuery(NotesContract.notes(), null, null, null, null, true)
                .mapToList(new Func1<Cursor, Note>() {
                    @Override public Note call(Cursor cursor) {
                        return Note.create(cursor);
                    }
                });
    }

    @Override
    public Observable<Note> get(Long noteId) {
        return briteContentResolver.createQuery(NotesContract.notes(), null, null, null, null, false)
                .mapToList(new Func1<Cursor, Note>() {
                    @Override public Note call(Cursor cursor) {
                        return Note.create(cursor);
                    }
                })
                .flatMap(new Func1<List<Note>, Observable<Note>>() {
                    @Override public Observable<Note> call(List<Note> notes) {
                        return Observable.from(notes);
                    }
                })
                .take(1);
    }

    @Override
    public Observable<Integer> clear() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(contentResolver.delete(NotesContract.notes(), null, null));
            }
        });
    }
}
