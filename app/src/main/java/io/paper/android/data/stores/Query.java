package io.paper.android.data.stores;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Query {

    @Nullable
    private final Long id;

    @Nullable
    private final String[] projection;

    @Nullable
    private final String selection;

    @Nullable
    private final String[] selectionArgs;

    @Nullable
    private final String sortOrder;

    @Nullable
    private final boolean notifyForDescendents;

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    private Query(@Nullable Long id, @Nullable String[] projection, @Nullable String selection,
                  @Nullable String[] selectionArgs, @Nullable String sortOrder, boolean notify) {
        this.id = id;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;
        this.notifyForDescendents = notify;
    }

    @Nullable
    @Deprecated
    public Long id() {
        return id;
    }

    @Nullable
    public String[] projection() {
        return projection;
    }

    @Nullable
    public String selection() {
        return selection;
    }

    @Nullable
    public String[] selectionArgs() {
        return selectionArgs;
    }

    @Nullable
    public String sortOrder() {
        return sortOrder;
    }

    public boolean notifyForDescendents() {
        return notifyForDescendents;
    }

    public static class Builder {
        private Long id;
        private String[] projection;
        private String selection;
        private String[] selectionArgs;
        private String sortOrder;
        private boolean notifyForDescendents;

        Builder() {
            // explicit constructor
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder projection(String[] projection) {
            this.projection = projection;
            return this;
        }

        public Builder selection(String selection) {
            this.selection = selection;
            return this;
        }

        public Builder selectionArgs(String[] selectionArgs) {
            this.selectionArgs = selectionArgs;
            return this;
        }

        public Builder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder notifyForDescendents(boolean notify) {
            this.notifyForDescendents = notify;
            return this;
        }

        public Query build() {
            return new Query(id, projection, selection, selectionArgs,
                    sortOrder, notifyForDescendents);
        }
    }
}
