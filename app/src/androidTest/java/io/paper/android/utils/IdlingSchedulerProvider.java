package io.paper.android.utils;

import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IdlingSchedulerProvider implements SchedulerProvider {
    private final DelegatingIdlingResourceScheduler computation;
    private final DelegatingIdlingResourceScheduler io;

    public IdlingSchedulerProvider() {
        computation = new DelegatingIdlingResourceScheduler(
                "RxJava computation scheduler", Schedulers.computation());
        io = new DelegatingIdlingResourceScheduler(
                "RxJava I/O scheduler", Schedulers.io());

        Espresso.registerIdlingResources(computation, io);
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return computation;
    }

    @NonNull
    @Override
    public Scheduler io() {
        return io;
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
