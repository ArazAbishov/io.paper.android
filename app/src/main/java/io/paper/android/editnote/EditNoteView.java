package io.paper.android.editnote;

import android.support.annotation.NonNull;

import io.paper.android.notes.Note;
import io.paper.android.ui.View;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public interface EditNoteView extends View {

    @NonNull
    Observable<Object> toolbar();

    @NonNull
    Observable<String> noteTitle();

    @NonNull
    Observable<String> noteDescription();

    @NonNull
    Consumer<Note> showNote();

    @NonNull
    Consumer<Object> navigateUp();
}
