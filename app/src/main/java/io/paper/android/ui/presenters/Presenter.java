package io.paper.android.ui.presenters;

import android.support.annotation.NonNull;

import io.paper.android.ui.views.View;

public interface Presenter {
    void attachView(@NonNull View view);

    void detachView();
}
