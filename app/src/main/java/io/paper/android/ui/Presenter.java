package io.paper.android.ui;

import android.support.annotation.NonNull;

public interface Presenter {
    void attachView(@NonNull View view);

    void detachView();
}
