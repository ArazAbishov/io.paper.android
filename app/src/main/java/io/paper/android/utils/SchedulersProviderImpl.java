package io.paper.android.utils;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SchedulersProviderImpl implements SchedulerProvider {

    @NonNull @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}