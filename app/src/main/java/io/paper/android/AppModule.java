package io.paper.android;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.paper.android.stores.DbModule;

@Module(
        includes = {
                DbModule.class
        }
)
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
}
