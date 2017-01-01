package io.paper.android.notes;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.paper.android.PaperApp;
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
    private static final String NOTE_TITLE = "Note title";
    private static final String NOTE_DESCRIPTION = "Note description";

    @Rule
    public ActivityTestRule<NotesActivity> notesActivityTestRule =
            new ActivityTestRule<NotesActivity>(NotesActivity.class) {
                @Override protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    // remove all notes before starting activity
                    PaperApp.getAppComponent(InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).notesRepository().clear().toBlocking().subscribe();
                }
            };

    @Test
    public void clickAddNoteButton_opensAddNoteUi() {
        // click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());

        onView(withId(R.id.edittext_note_title)).perform(
                typeText(NOTE_TITLE), closeSoftKeyboard());
        onView(withId(R.id.edittext_note_description)).perform(
                typeText(NOTE_DESCRIPTION), closeSoftKeyboard());

        // go back to the list of the notes
        Espresso.pressBack();

        // scroll notes list to added note, by finding its description
        onView(withId(R.id.recyclerview_notes)).perform(
                scrollTo(hasDescendant(withText(NOTE_TITLE))));

        onView(withItemText(NOTE_TITLE)).check(matches(isDisplayed()));
    }

    @Test
    public void tearDown() {
        // we need to make sure that repository does not contain any state from execution of other tests
        PaperApp.getAppComponent(InstrumentationRegistry.getTargetContext()
                .getApplicationContext()).notesRepository().clear().toBlocking().subscribe();
    }
}
