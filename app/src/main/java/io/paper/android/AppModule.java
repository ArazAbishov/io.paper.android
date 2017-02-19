package io.paper.android;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.paper.android.commons.database.DbModule;
import io.paper.android.commons.schedulers.SchedulersModule;

@Module(
        includes = {
                DbModule.class, SchedulersModule.class
        }
)
final class AppModule {
    private final Application application;

    AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application application() {
        return application;
    }
}
