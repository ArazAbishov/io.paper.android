package io.paper.android.notes;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class NotesTest {

    @Test
    public void equals_shouldConformToContract() {
        // in order to get class which implements Note,
        // we need to create instance first

        Note note = Note.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .build();

        EqualsVerifier.forClass(note.getClass())
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }
}
