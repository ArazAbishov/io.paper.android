package io.paper.android.data.stores;

import android.support.annotation.Nullable;

public final class Where {
    @Nullable
    private final String where;

    @Nullable
    private final String[] arguments;

    private Where(@Nullable String where, @Nullable String[] arguments) {
        this.where = where;
        this.arguments = arguments;
    }

    public String where() {
        return where;
    }

    public String[] arguments() {
        return arguments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        @Nullable
        private String where;

        @Nullable
        private String[] arguments;

        Builder() {
            // empty constructor
        }

        public Builder where(String where) {
            this.where = where;
            return this;
        }

        public Builder arguments(String... arguments) {
            this.arguments = arguments;
            return this;
        }

        public Where build() {
            return new Where(where, arguments);
        }
    }
}
