package io.paper.android.notes;

import java.util.List;

import rx.Observable;

public interface NotesRepository {
    Observable<Long> add(String title, String description);

    Observable<Integer> putTitle(Long noteId, String title);

    Observable<Integer> putDescription(Long noteId, String description);

    Observable<List<Note>> list();

    Observable<Note> get(Long noteId);

    Observable<Integer> clear();
}
