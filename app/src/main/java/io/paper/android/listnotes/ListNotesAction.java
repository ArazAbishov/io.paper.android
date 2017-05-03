package io.paper.android.listnotes;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import io.paper.android.notes.Note;

@AutoValue
abstract class ListNotesAction {

    @NonNull
    abstract Note note();

    @NonNull
    static ListNotesAction click(@NonNull Note note) {
        return new AutoValue_ListNotesAction(note);
    }
}
