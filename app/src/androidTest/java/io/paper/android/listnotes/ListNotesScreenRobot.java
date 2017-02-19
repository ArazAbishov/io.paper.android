package io.paper.android.listnotes;

import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;

import io.paper.android.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.paper.android.espresso.ViewActions.withItemText;

final class ListNotesScreenRobot {
    ListNotesScreenRobot() {
        // explicit empty constructor
    }

    @NonNull
    ListNotesScreenRobot addNewNote() {
        // click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());
        return this;
    }

    @NonNull
    ListNotesScreenRobot enterTitle(@NonNull String title) {
        onView(withId(R.id.edittext_note_title)).perform(
                typeText(title), closeSoftKeyboard());
        return this;
    }

    @NonNull
    ListNotesScreenRobot enterDescription(@NonNull String description) {
        onView(withId(R.id.edittext_note_description)).perform(
                typeText(description), closeSoftKeyboard());
        return this;
    }

    @NonNull
    ListNotesScreenRobot pressBack() {
        Espresso.pressBack();
        return this;
    }

    @NonNull
    ListNotesScreenRobot lookForNote(@NonNull String title) {
        onView(withId(R.id.recyclerview_notes)).perform(
                scrollTo(hasDescendant(withText(title))));
        onView(withItemText(title)).check(matches(isDisplayed()));
        return this;
    }
}
