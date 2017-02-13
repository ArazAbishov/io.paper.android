package io.paper.android.notes;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.paper.android.PaperApp;
import io.paper.android.utils.Screenshot;

@RunWith(AndroidJUnit4.class)
public class NotesScreenTest {
    private static final String NOTE_TITLE = "Note title";
    private static final String NOTE_DESCRIPTION = "Note description";

    private NotesScreenRobot notesScreenRobot;

    @Before
    public void setUp() throws Exception {
        notesScreenRobot = new NotesScreenRobot();
    }

    @Rule
    @SuppressWarnings("CheckReturnValue")
    public ActivityTestRule<NotesActivity> notesActivityTestRule =
            new ActivityTestRule<NotesActivity>(NotesActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    // remove all notes before starting activity
                    PaperApp.getAppComponent(InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).notesRepository().clear().blockingFirst();
                }
            };

    @Test
    public void clickAddNoteButton_opensAddNoteUi() {
        Screenshot.capture(notesActivityTestRule.getActivity(), "state_before_adding_note");

        notesScreenRobot.addNewNote()
                .enterTitle(NOTE_TITLE)
                .enterDescription(NOTE_DESCRIPTION)
                .pressBack()
                .lookForNote(NOTE_TITLE);

        Screenshot.capture(notesActivityTestRule.getActivity(), "state_after_adding_note");
    }

    @After
    @SuppressWarnings("CheckReturnValue")
    public void tearDown() {
        // we need to make sure that repository does not contain any state from execution of other tests
        PaperApp.getAppComponent(InstrumentationRegistry.getTargetContext()
                .getApplicationContext()).notesRepository().clear().blockingFirst();
    }
}
