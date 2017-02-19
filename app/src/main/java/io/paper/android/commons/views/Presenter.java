package io.paper.android.commons.views;

import android.support.annotation.NonNull;

public interface Presenter {
    void attachView(@NonNull View view);

    void detachView();
}
