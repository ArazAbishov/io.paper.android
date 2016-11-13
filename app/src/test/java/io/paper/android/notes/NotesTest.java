package io.paper.android.notes;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.assertj.core.api.Java6Assertions.assertThat;

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

    @Test
    public void builder_shouldPropagatePropertiesAsExpected() {
        Note note = Note.builder()
                .id(10L)
                .title("title")
                .description("description")
                .build();

        assertThat(note.id()).isEqualTo(10L);
        assertThat(note.title()).isEqualTo("title");
        assertThat(note.description()).isEqualTo("description");
    }
}
