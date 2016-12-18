package io.paper.android.notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import rx.Observable;
import rx.functions.Func0;

// ToDo add tests to repository in order to verify reactive behaviour of list() and get()
class FakeNotesRepositoryImpl implements NotesRepository {
    private final AtomicLong counter;
    private final Map<Long, Note> storage;

    FakeNotesRepositoryImpl() {
        this.counter = new AtomicLong();
        this.storage = new HashMap<>();
    }

    @Override
    public Observable<Long> add(final String title, final String description) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                Long noteId = counter.incrementAndGet();

                Note note = Note.builder()
                        .id(noteId)
                        .title(title)
                        .description(description)
                        .build();
                // modify id of note
                storage.put(noteId, note);

                // put note into storage
                return Observable.just(noteId);
            }
        });
    }

    @Override
    public Observable<Integer> putTitle(final Long noteId, final String title) {
        if (!storage.containsKey(noteId)) {
            throw new RuntimeException("No such row: id = " + noteId);
        }

        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                Note note = storage.get(noteId);

                // update note value
                note = note.toBuilder().title(title).build();

                // but it back to storage
                storage.put(noteId, note);

                return Observable.just(1);
            }
        });
    }

    @Override
    public Observable<Integer> putDescription(final Long noteId, final String description) {
        if (!storage.containsKey(noteId)) {
            throw new RuntimeException("No such row: id = " + noteId);
        }

        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                Note note = storage.get(noteId);

                // update note value
                note = note.toBuilder().description(description).build();

                // but it back to storage
                storage.put(noteId, note);

                return Observable.just(1);
            }
        });
    }

    @Override
    public Observable<List<Note>> list() {
        return Observable.defer(new Func0<Observable<List<Note>>>() {
            @Override
            public Observable<List<Note>> call() {
                return Observable.just((List<Note>) new ArrayList<>(storage.values()));
            }
        });
    }

    @Override
    public Observable<Note> get(final Long noteId) {
        if (!storage.containsKey(noteId)) {
            throw new RuntimeException("No such row: id = " + noteId);
        }

        return Observable.defer(new Func0<Observable<Note>>() {
            @Override
            public Observable<Note> call() {
                return Observable.just(storage.get(noteId));
            }
        });
    }

    @Override
    public Observable<Integer> clear() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                // total count of rows
                int count = storage.size();

                storage.clear();

                return Observable.just(count);
            }
        });
    }
}
