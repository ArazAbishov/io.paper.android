package io.paper.android.data;

import android.net.Uri;

public final class DbContract {
    public static final String AUTHORITY = "io.paper.android.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private DbContract() {
        // no instances
    }
}
