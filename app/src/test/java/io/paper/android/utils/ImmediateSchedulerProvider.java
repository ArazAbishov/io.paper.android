package io.paper.android.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


public class ImmediateSchedulerProvider implements SchedulerProvider {
    private final Executor currentThreadExecutor;

    public ImmediateSchedulerProvider() {
        this.currentThreadExecutor = Runnable::run;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.from(currentThreadExecutor);
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.from(currentThreadExecutor);
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return Schedulers.from(currentThreadExecutor);
    }
}
