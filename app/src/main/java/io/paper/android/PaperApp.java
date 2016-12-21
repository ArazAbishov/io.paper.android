package io.paper.android;

import android.app.Application;
import android.content.Context;

import io.paper.android.data.DbModule;
import io.paper.android.editnote.EditNoteComponent;
import io.paper.android.editnote.EditNoteModule;

// ToDo: Add more tests for data layer (ContentProvider, Models)
// ToDo: Integrate gradle plugin for jacoco (test coverage)
// ToDo: Integrate more plugins for static analysis (findbugs, checkstyle)
// ToDo: Add specific configuration to VM to track unclosed cursors and database sessions
public class PaperApp extends Application {
    private static final String DATABASE_NAME = "paper.db";

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = prepareAppComponent()
                .build();
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
