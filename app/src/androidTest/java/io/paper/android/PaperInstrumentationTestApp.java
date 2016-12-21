package io.paper.android;

import io.paper.android.data.DbModule;

public class PaperInstrumentationTestApp extends PaperApp {

    @Override
    protected DaggerAppComponent.Builder prepareAppComponent() {
        return super.prepareAppComponent()
                .dbModule(new DbModule(null));
    }
}
