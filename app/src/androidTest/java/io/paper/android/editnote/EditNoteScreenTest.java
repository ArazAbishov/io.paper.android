package io.paper.android.editnote;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.paper.android.PaperApp;
import io.paper.android.notes.NotesRepository;
import io.paper.android.utils.Screenshot;

@RunWith(AndroidJUnit4.class)
public class EditNoteScreenTest {
    private static final String NOTE_TITLE = "Fancy note title";
    private static final String NOTE_DESCRIPTION = "Fancy note description";

    private NotesRepository notesRepository;
    private EditNoteScreenRobot editNoteScreenRobot;

    @Rule
    public ActivityTestRule<EditNoteActivity> editNoteActivityRule =
            new ActivityTestRule<>(EditNoteActivity.class, true, false);

    @Before
    public void setUp() {
        editNoteScreenRobot = new EditNoteScreenRobot();

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
        editNoteScreenRobot
                .checkTitle(NOTE_TITLE)
                .checkDescription(NOTE_DESCRIPTION);

        Screenshot.capture(editNoteActivityRule.getActivity(), "current_state_of_the_note");
    }

    @After
    @SuppressWarnings("CheckReturnValue")
    public void tearDown() {
        // we need to make sure that repository does not contain
        // any state from execution of other tests
        notesRepository.clear();
    }
}
