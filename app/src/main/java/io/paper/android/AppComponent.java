package io.paper.android;

import javax.inject.Singleton;

import dagger.Component;
import io.paper.android.notes.AddNoteFragment;
import io.paper.android.notes.NotesFragment;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(NotesFragment notesFragment);

    void inject(AddNoteFragment addNoteFragment);
}