package io.paper.android;

import javax.inject.Singleton;

import dagger.Component;
import io.paper.android.notes.NotesComponent;
import io.paper.android.notes.NotesModule;

@Singleton
@Component(modules = {
        AppModule.class
})
interface AppComponent {
    NotesComponent plus(NotesModule notesModule);
}
