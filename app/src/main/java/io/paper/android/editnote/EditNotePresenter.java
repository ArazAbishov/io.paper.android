package io.paper.android.editnote;

import android.support.annotation.NonNull;

import io.paper.android.ui.Presenter;

public interface EditNotePresenter extends Presenter {
    void updateTitle(@NonNull String title);

    void updateDescription(@NonNull String description);
}
