package io.paper.android.editnote;

import android.support.annotation.NonNull;

import io.paper.android.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

final class EditNoteScreenRobot {
    EditNoteScreenRobot() {
        // explicit empty constructor
    }

    @NonNull
    EditNoteScreenRobot checkTitle(@NonNull String title) {
        onView(withId(R.id.edittext_note_title)).check(matches(withText(title)));
        return this;
    }

    @NonNull
    EditNoteScreenRobot checkDescription(@NonNull String description) {
        onView(withId(R.id.edittext_note_description)).check(matches(withText(description)));
        return this;
    }
}
