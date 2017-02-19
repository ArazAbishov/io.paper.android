package io.paper.android;

import io.paper.android.commons.database.DbModule;
import io.paper.android.commons.schedulers.IdlingSchedulerProvider;
import io.paper.android.commons.schedulers.SchedulersModule;

public class PaperInstrumentationTestApp extends PaperApp {

    @Override
    protected DaggerAppComponent.Builder prepareAppComponent() {
        return super.prepareAppComponent()
                .dbModule(new DbModule(null))
                .schedulersModule(new SchedulersModule(new IdlingSchedulerProvider()));
    }
}
