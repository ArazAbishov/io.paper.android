package io.paper.android;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import hu.supercluster.paperwork.Paperwork;
import io.fabric.sdk.android.Fabric;
import io.paper.android.commons.CrashReportingTree;
import io.paper.android.commons.dagger.PerSession;
import io.paper.android.commons.database.DbModule;
import io.paper.android.commons.schedulers.SchedulersModule;
import io.paper.android.commons.schedulers.SchedulersProviderImpl;
import io.paper.android.notes.NotesComponent;
import io.paper.android.notes.NotesModule;
import timber.log.Timber;

public class PaperApp extends Application {
    private static final String DATABASE_NAME = "paper.db";
    private static final String GIT_SHA = "gitSha";
    private static final String BUILD_DATE = "buildDate";

    @Singleton
    private AppComponent appComponent;

    @PerSession
    private NotesComponent notesComponent;

    @Nullable
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        refWatcher = setUpLeakCanary();
        appComponent = prepareAppComponent().build();
        notesComponent = setUpNotesComponent();

        Paperwork paperwork = setUpPaperwork();
        setUpFabric(paperwork);
        setUpTimber();

        setUpStrictMode();
    }

    public RefWatcher refWatcher() {
        return refWatcher;
    }

    public AppComponent appComponent() {
        return appComponent;
    }

    public NotesComponent notesComponent() {
        return notesComponent;
    }

    protected DaggerAppComponent.Builder prepareAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule(DATABASE_NAME))
                .schedulersModule(new SchedulersModule(new SchedulersProviderImpl()));
    }

    @NonNull
    private RefWatcher setUpLeakCanary() {
        if (BuildConfig.DEBUG) {
            return LeakCanary.install(this);
        } else {
            return RefWatcher.DISABLED;
        }
    }

    @NonNull
    private Paperwork setUpPaperwork() {
        return new Paperwork(this);
    }

    @NonNull
    private NotesComponent setUpNotesComponent() {
        return appComponent.plus(new NotesModule());
    }

    private void setUpFabric(@NonNull Paperwork paperwork) {
        if (BuildConfig.DEBUG) {
            // Set up Crashlytics, disabled for debug builds
            Crashlytics crashlyticsKit = new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder()
                            .disabled(true)
                            .build())
                    .build();

            // Initialize Fabric with the debug-disabled crashlytics.
            Fabric.with(this, crashlyticsKit);
        } else {
            Crashlytics crashlytics = new Crashlytics();
            crashlytics.core.setString(GIT_SHA, paperwork.get(GIT_SHA));
            crashlytics.core.setString(BUILD_DATE, paperwork.get(BUILD_DATE));

            Fabric.with(this, new Crashlytics());
            Fabric.with(this, new Answers());
        }
    }

    private void setUpTimber() {
        if (BuildConfig.DEBUG) {
            // Verbose logging for debug builds.
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void setUpStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }
}
