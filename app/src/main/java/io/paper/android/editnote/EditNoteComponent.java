package io.paper.android.editnote;

import dagger.Subcomponent;
import io.paper.android.ui.ActivityScope;

@ActivityScope
@Subcomponent(modules = {
        EditNoteModule.class
})
public interface EditNoteComponent {
    void inject(EditNoteFragment editNoteFragment);
}
