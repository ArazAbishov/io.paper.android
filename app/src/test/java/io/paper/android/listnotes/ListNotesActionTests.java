package io.paper.android.listnotes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.paper.android.notes.Note;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class ListNotesActionTests {

    @Test
    public void equalsAndHashcodeShouldConformToContract() {
        EqualsVerifier.forClass(ListNotesAction.click(mock(Note.class)).getClass())
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    public void shouldThrowOnNullNote() {
        try {
            ListNotesAction.click(null);
            fail("NullPointerException was expected, but nothing was thrown");
        } catch (NullPointerException nullPointerException) {
            // noop
        }
    }

    @Test
    public void noteShouldBePropagated() {
        Note note = mock(Note.class);
        ListNotesAction action = ListNotesAction.click(note);

        assertThat(action.note()).isEqualTo(note);
    }
}
