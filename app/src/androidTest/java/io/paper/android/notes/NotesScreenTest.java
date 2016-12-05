package io.paper.android.notes;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

@RunWith(AndroidJUnit4.class)
public class NotesScreenTest {

    @Rule
    public ActivityTestRule<NotesActivity> notesActivityTestRule =
            new ActivityTestRule<>(NotesActivity.class);

    @Test
    public void clickAddNoteButton_opensAddNoteUi() {
        String newNoteTitle = "Fancy note title";
        String newNoteDescription = "Fancy note description";

        // click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());

        onView(withId(R.id.edittext_note_title)).perform(
                typeText(newNoteTitle), closeSoftKeyboard());
        onView(withId(R.id.edittext_note_description)).perform(
                typeText(newNoteDescription), closeSoftKeyboard());

        // go back to the list of the notes
        Espresso.pressBack();

        // scroll notes list to added note, by finding its description
        onView(withId(R.id.recyclerview_notes)).perform(
                scrollTo(hasDescendant(withText(newNoteTitle))));

        onView(withItemText(newNoteTitle)).check(matches(isDisplayed()));
    }
}
