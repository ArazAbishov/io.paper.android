package io.paper.android.notes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.paper.android.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NotesScreenTest {

    @Rule
    public ActivityTestRule<NotesActivity> notesActivityTestRule =
            new ActivityTestRule<>(NotesActivity.class);

    @Test
    public void clickAddNoteButton_opensAddNoteUi() {
        // throw new RuntimeException();
        // click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());

        // check if add note screen is displayed
        onView(withId(R.id.edittext_note_title)).check(matches(isDisplayed()));
    }
}
