package io.paper.android.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.squareup.spoon.Spoon;

public final class Screenshot {
    private Screenshot() {
        // private constructor
    }

    public static void capture(@NonNull Activity activity, @NonNull String tag) {
        InstrumentationRegistry.getArguments();
        // take screenshot for firebase
        ScreenShotter.takeScreenshot(tag, activity);

        // take screenshot for spoon
        Spoon.screenshot(activity, tag);
    }
}
