package io.paper.android.editnote;

import dagger.Subcomponent;
import io.paper.android.commons.dagger.PerView;

@PerView
@Subcomponent(modules = {
        EditNoteModule.class
})
public interface EditNoteComponent {
    void inject(EditNoteFragment editNoteFragment);
}
