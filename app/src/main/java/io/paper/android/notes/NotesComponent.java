package io.paper.android.notes;

import dagger.Subcomponent;
import io.paper.android.commons.dagger.PerSession;
import io.paper.android.editnote.EditNoteComponent;
import io.paper.android.editnote.EditNoteModule;

@PerSession
@Subcomponent(modules = {
        NotesModule.class
})
public interface NotesComponent {
    NotesRepository notesRepository();

    EditNoteComponent plus(EditNoteModule editNoteModule);
}
