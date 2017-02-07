package io.paper.android.utils;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SchedulersModule {
    private final SchedulerProvider schedulerProvider;

    public SchedulersModule(@NonNull SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    @Provides
    @Singleton
    SchedulerProvider providesSchedulerProvider() {
        return schedulerProvider;
    }
}
