package io.paper.android.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Book implements Model {
    public abstract String title();
    public abstract String description();

    public static Builder builder() {
        return new AutoValue_Book.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long id);
        public abstract Builder title(String title);
        public abstract Builder description(String description);
        public abstract Book build();
    }
}
