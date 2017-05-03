package io.paper.android.listnotes;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import io.paper.android.notes.Note;

@AutoValue
public abstract class ListNotesAction {

    @NonNull
    public abstract Note note();

    @NonNull
    public static ListNotesAction click(@NonNull Note note) {
        return new AutoValue_ListNotesAction(note);
    }
}
