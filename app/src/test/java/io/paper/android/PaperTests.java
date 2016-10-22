package io.paper.android;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import io.paper.android.notes.NotesPresenterTest;
import io.paper.android.notes.NotesTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NotesTest.class,
        NotesPresenterTest.class
})
public class PaperTests {
}
