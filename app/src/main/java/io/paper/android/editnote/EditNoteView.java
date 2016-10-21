package io.paper.android.editnote;

import android.support.annotation.NonNull;

import io.paper.android.notes.Note;
import io.paper.android.ui.View;

public interface EditNoteView extends View {
    void showNote(@NonNull Note note);
}
