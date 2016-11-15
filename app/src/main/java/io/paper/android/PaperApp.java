package io.paper.android;

import android.app.Application;
import android.content.Context;

import io.paper.android.data.stores.DbModule;
import io.paper.android.editnote.EditNoteComponent;
import io.paper.android.editnote.EditNoteModule;

// ToDo: Implement mock flavor of the app with fake repository implementation
// ToDo: Integrate gradle plugin for jacoco (test coverage)
// ToDo: Integrate more plugins for static analysis (findbugs, checkstyle)
// ToDo: Add specific configuration to VM to track unclosed cursors and database sessions
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

    public static EditNoteComponent getEditNoteComponent(Context context, Long noteId) {
        return ((PaperApp) context.getApplicationContext())
                .appComponent.plus(new EditNoteModule(noteId));
    }
}
