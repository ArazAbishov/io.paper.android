package io.paper.android.editnote;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.notes.NotesRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EditNoteScreenTest {
    private static final String NOTE_TITLE = "Fancy note title";
    private static final String NOTE_DESCRIPTION = "Fancy note description";

    private NotesRepository notesRepository;

    @Rule
    public ActivityTestRule<EditNoteActivity> editNoteActivityRule =
            new ActivityTestRule<>(EditNoteActivity.class, true, false);

    @Before
    public void setUp() {
        // add note to fake repository synchronously
        notesRepository = PaperApp.getAppComponent(InstrumentationRegistry.getTargetContext()
                .getApplicationContext()).notesRepository();
        Long noteId = notesRepository.add(NOTE_TITLE, NOTE_DESCRIPTION).blockingFirst();

        // start activity
        Intent intent = new Intent();
        intent.putExtra(EditNoteActivity.ARG_NOTE_ID, noteId);
        editNoteActivityRule.launchActivity(intent);
    }

    @Test
    public void noteDetails_displayedInUi() {
        // Check that the note title are description are displayed
        onView(withId(R.id.edittext_note_title)).check(matches(withText(NOTE_TITLE)));
        onView(withId(R.id.edittext_note_description)).check(matches(withText(NOTE_DESCRIPTION)));

        Spoon.screenshot(editNoteActivityRule.getActivity(), "current_state_of_the_note");
    }

    @Test
    @SuppressWarnings("CheckReturnValue")
    public void tearDown() {
        // we need to make sure that repository does not contain any state from execution of other tests
        PaperApp.getAppComponent(InstrumentationRegistry.getTargetContext()
                .getApplicationContext()).notesRepository().clear().blockingFirst();
    }
}
