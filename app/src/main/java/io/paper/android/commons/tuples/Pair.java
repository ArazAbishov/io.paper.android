package io.paper.android.commons.tuples;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Pair<A, B> {

    @Nullable
    public abstract A val0();

    @Nullable
    public abstract B val1();

    @NonNull
    public static <A, B> Pair<A, B> create(@Nullable A val0, @Nullable B val1) {
        return new AutoValue_Pair<>(val0, val1);
    }
}
