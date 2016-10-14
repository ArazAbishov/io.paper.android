package io.paper.android.models;

import com.google.auto.value.AutoValue;

/**
 * Immutable model which represents Note
 */
@AutoValue
public abstract class Note implements Model {
    public abstract String title();
    public abstract String description();

    public static Builder builder() {
        return new AutoValue_Note.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long id);
        public abstract Builder title(String title);
        public abstract Builder description(String description);
        public abstract Note build();
    }
}
