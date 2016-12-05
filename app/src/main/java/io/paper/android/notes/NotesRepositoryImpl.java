package io.paper.android.notes;

import android.content.ContentValues;

import java.util.List;

import io.paper.android.data.stores.Query;
import io.paper.android.data.stores.Store;
import rx.Observable;
import rx.functions.Func1;

class NotesRepositoryImpl implements NotesRepository {
    private final Store<Note> noteStore;

    NotesRepositoryImpl(Store<Note> noteStore) {
        this.noteStore = noteStore;
    }

    @Override
    public Observable<Long> add(Note note) {
        return noteStore.insert(note);
    }

    @Override
    public Observable<Integer> putTitle(Long noteId, String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.COLUMN_TITLE, title);
        return noteStore.update(noteId, contentValues);
    }

    @Override
    public Observable<Integer> putDescription(Long noteId, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.COLUMN_DESCRIPTION, description);
        return noteStore.update(noteId, contentValues);
    }

    @Override
    public Observable<List<Note>> list() {
        return noteStore.query(Query.builder().notifyForDescendents(true).build());
    }

    @Override
    public Observable<Note> get(Long noteId) {
        return noteStore.query(Query.builder().id(noteId)
                .notifyForDescendents(false).build())
                .flatMap(new Func1<List<Note>, Observable<Note>>() {
                    @Override
                    public Observable<Note> call(List<Note> notes) {
                        return Observable.from(notes);
                    }
                })
                .take(1);
    }
}
