package io.paper.android;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import io.paper.android.data.DbModule;
import io.paper.android.editnote.EditNoteComponent;
import io.paper.android.editnote.EditNoteModule;
import io.paper.android.utils.CrashReportingTree;
import timber.log.Timber;

// ToDo: Add more tests for data layer (ContentProvider, Models)
// ToDo: Add specific configuration to VM to track unclosed cursors and database sessions
public class PaperApp extends Application {
    private static final String DATABASE_NAME = "paper.db";

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

            // Set up Crashlytics, disabled for debug builds
            Crashlytics crashlyticsKit = new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder()
                            .disabled(true)
                            .build())
                    .build();

            // Initialize Fabric with the debug-disabled crashlytics.
            Fabric.with(this, crashlyticsKit);
        } else {
            Fabric.with(this, new Crashlytics());
            Fabric.with(this, new Answers());

            Timber.plant(new CrashReportingTree());
        }

        appComponent = prepareAppComponent().build();
    }

    protected DaggerAppComponent.Builder prepareAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule(DATABASE_NAME));
    }

    public static AppComponent getAppComponent(Context context) {
        return ((PaperApp) context.getApplicationContext()).appComponent;
    }

    public static EditNoteComponent getEditNoteComponent(Context context, Long noteId) {
        return ((PaperApp) context.getApplicationContext())
                .appComponent.plus(new EditNoteModule(noteId));
    }
}
