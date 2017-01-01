package io.paper.android.notes;

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class NoteIntegrationTests {
    private static final long ID = 1L;
    private static final String TITLE = "Note";
    private static final String DESCRIPTION = "Note description";

    @Test
    public void toContentValues_shouldMapToContentValues() {
        Note note = Note.builder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .build();

        ContentValues contentValues = note.toContentValues();
        assertThat(contentValues.getAsLong(Note.Columns.ID)).isEqualTo(ID);
        assertThat(contentValues.getAsString(Note.Columns.TITLE)).isEqualTo(TITLE);
        assertThat(contentValues.getAsString(Note.Columns.DESCRIPTION)).isEqualTo(DESCRIPTION);
    }

    @Test
    public void create_shouldMapFromCursor() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                Note.Columns.ID,
                Note.Columns.TITLE,
                Note.Columns.DESCRIPTION
        });

        matrixCursor.addRow(new Object[]{
                ID, TITLE, DESCRIPTION
        });

        matrixCursor.moveToFirst();

        Note note = Note.create(matrixCursor);
        assertThat(note.id()).isEqualTo(ID);
        assertThat(note.title()).isEqualTo(TITLE);
        assertThat(note.description()).isEqualTo(DESCRIPTION);
    }
}
