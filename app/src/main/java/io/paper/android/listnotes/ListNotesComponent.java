package io.paper.android.listnotes;

import dagger.Subcomponent;
import io.paper.android.commons.dagger.PerView;

@PerView
@Subcomponent(modules = ListNotesModule.class)
public interface ListNotesComponent {
    void inject(ListNotesFragment listNotesFragment);
}
