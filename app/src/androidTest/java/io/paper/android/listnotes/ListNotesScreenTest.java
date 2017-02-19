package io.paper.android.listnotes;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.spoon.Spoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.paper.android.PaperApp;

@RunWith(AndroidJUnit4.class)
public class ListNotesScreenTest {
    private static final String NOTE_TITLE = "Note title";
    private static final String NOTE_DESCRIPTION = "Note description";

    private ListNotesScreenRobot listNotesScreenRobot;

    @Before
    public void setUp() throws Exception {
        listNotesScreenRobot = new ListNotesScreenRobot();
    }

    @Rule
    @SuppressWarnings("CheckReturnValue")
    public ActivityTestRule<ListNotesActivity> notesActivityTestRule =
            new ActivityTestRule<ListNotesActivity>(ListNotesActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    // remove all notes before starting activity
                    PaperApp paperApp = (PaperApp) InstrumentationRegistry
                            .getTargetContext().getApplicationContext();
                    paperApp.notesComponent().notesRepository().clear().blockingFirst();
                }
            };

    @Test
    public void clickAddNoteButton_opensAddNoteUi() {
        Spoon.screenshot(notesActivityTestRule.getActivity(), "state_before_adding_note");

        listNotesScreenRobot.addNewNote()
                .enterTitle(NOTE_TITLE)
                .enterDescription(NOTE_DESCRIPTION)
                .pressBack()
                .lookForNote(NOTE_TITLE);

        Spoon.screenshot(notesActivityTestRule.getActivity(), "state_after_adding_note");
    }

    @After
    @SuppressWarnings("CheckReturnValue")
    public void tearDown() {
        // we need to make sure that repository does not contain any state from execution of other tests
        PaperApp paperApp = (PaperApp) InstrumentationRegistry
                .getTargetContext().getApplicationContext();
        paperApp.notesComponent().notesRepository().clear();
    }
}
