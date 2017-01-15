package io.paper.android;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import hu.supercluster.paperwork.Paperwork;
import io.fabric.sdk.android.Fabric;
import io.paper.android.data.DbModule;
import io.paper.android.editnote.EditNoteComponent;
import io.paper.android.editnote.EditNoteModule;
import io.paper.android.utils.CrashReportingTree;
import timber.log.Timber;

// ToDo: Add more tests for data layer (Stores, Models)
public class PaperApp extends Application {
    private static final String DATABASE_NAME = "paper.db";
    private static final String GIT_SHA = "gitSha";
    private static final String BUILD_DATE = "buildDate";

    private AppComponent appComponent;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        appComponent = prepareAppComponent().build();
        refWatcher = setUpLeakCanary();

        Paperwork paperwork = setUpPaperwork();
        setUpFabric(paperwork);
        setUpTimber();
        setStrictMode();
    }

    public static AppComponent getAppComponent(Context context) {
        return ((PaperApp) context.getApplicationContext()).appComponent;
    }

    public static EditNoteComponent getEditNoteComponent(Context context, Long noteId) {
        return ((PaperApp) context.getApplicationContext())
                .appComponent.plus(new EditNoteModule(noteId));
    }

    public static RefWatcher refWatcher(Context context) {
        return ((PaperApp) context.getApplicationContext()).refWatcher;
    }

    protected DaggerAppComponent.Builder prepareAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule(DATABASE_NAME));
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

    private void setStrictMode() {
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
