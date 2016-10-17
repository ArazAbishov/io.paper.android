package io.paper.android.editnote;

import android.support.annotation.NonNull;

import io.paper.android.ui.Presenter;

public interface EditNotePresenter extends Presenter {
    void updateTitle(@NonNull CharSequence title);

    void updateDescription(@NonNull CharSequence description);
}
