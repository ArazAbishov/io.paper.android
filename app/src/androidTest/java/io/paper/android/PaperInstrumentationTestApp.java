package io.paper.android;

import io.paper.android.data.DbModule;
import io.paper.android.utils.IdlingSchedulerProvider;
import io.paper.android.utils.SchedulersModule;

public class PaperInstrumentationTestApp extends PaperApp {

    @Override
    protected DaggerAppComponent.Builder prepareAppComponent() {
        return super.prepareAppComponent()
                .dbModule(new DbModule(null))
                .schedulersModule(new SchedulersModule(new IdlingSchedulerProvider()));
    }
}
