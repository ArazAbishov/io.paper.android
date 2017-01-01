package io.paper.android.notes;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NotesTest {
    private static final long ID = 10L;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    @Test
    public void equals_shouldConformToContract() {
        EqualsVerifier.forClass(Note.builder().build().getClass())
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    public void builder_shouldPropagatePropertiesAsExpected() {
        Note note = Note.builder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .build();

        assertThat(note.id()).isEqualTo(ID);
        assertThat(note.title()).isEqualTo(TITLE);
        assertThat(note.description()).isEqualTo(DESCRIPTION);
    }
}
