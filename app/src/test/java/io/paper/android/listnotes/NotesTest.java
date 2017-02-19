package io.paper.android.listnotes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.paper.android.notes.Note;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(JUnit4.class)
public class NotesTest {

    @Test
    public void equals_shouldConformToContract() {
        EqualsVerifier.forClass(Note.builder().build().getClass())
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

    @Test
    public void toBuilder_shouldPropagatePropertiesAsExpected() {
        Note note = Note.builder()
                .id(10L)
                .title("title")
                .description("description")
                .build();

        Note anotherTitle = note.toBuilder()
                .id(20L)
                .title("another_title")
                .description("another_description")
                .build();

        assertThat(anotherTitle.id()).isEqualTo(20L);
        assertThat(anotherTitle.title()).isEqualTo("another_title");
        assertThat(anotherTitle.description()).isEqualTo("another_description");
    }
}
