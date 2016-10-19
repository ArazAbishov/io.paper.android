package io.paper.android;

import javax.inject.Singleton;

import dagger.Component;
import io.paper.android.editnote.EditNoteComponent;
import io.paper.android.editnote.EditNoteModule;
import io.paper.android.notes.NotesFragment;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(NotesFragment notesFragment);

    EditNoteComponent plus(EditNoteModule editNoteModule);
}
