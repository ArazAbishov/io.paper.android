package io.paper.android;

import android.app.Application;
import android.content.Context;

import io.paper.android.data.stores.DbModule;

public final class PaperApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .build();
    }

    public static AppComponent getAppComponent(Context context) {
        return ((PaperApp) context.getApplicationContext()).appComponent;
    }
}
