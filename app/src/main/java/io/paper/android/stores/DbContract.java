package io.paper.android.stores;

import android.net.Uri;

public final class DbContract {
    public static final String AUTHORITY = "io.paper.android.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private DbContract() {
        // no instances
    }
}
