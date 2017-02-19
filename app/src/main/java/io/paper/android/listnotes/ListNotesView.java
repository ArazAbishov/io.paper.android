package io.paper.android.listnotes;

import android.support.annotation.NonNull;

import java.util.List;

import io.paper.android.notes.Note;
import io.paper.android.commons.views.View;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

interface ListNotesView extends View {

    @NonNull
    Observable<Object> createNoteButtonClicks();

    @NonNull
    Consumer<List<Note>> showNotes();

    @NonNull
    Consumer<Long> navigateToEditNoteView();
}
