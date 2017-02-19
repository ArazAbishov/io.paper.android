package io.paper.android.editnote;

import android.support.annotation.NonNull;

import io.paper.android.notes.Note;
import io.paper.android.commons.views.View;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

interface EditNoteView extends View {

    @NonNull
    Observable<Object> toolbarNavigationButtonClicks();

    @NonNull
    Observable<String> noteTitleFieldChanges();

    @NonNull
    Observable<String> noteDescriptionFieldChanges();

    @NonNull
    Consumer<Note> showNote();

    @NonNull
    Consumer<Object> navigateUp();
}
