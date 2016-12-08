package io.paper.android.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.test.ProviderTestCase2;

import io.paper.android.notes.NotesContract;

import static com.google.common.truth.Truth.assertThat;
import static io.paper.android.data.CursorAssert.assertThatCursor;

public class ContentProviderIntegrationTests extends ProviderTestCase2<DbContentProvider> {
    private static final String PROJECTION[] = {
            NotesContract.Columns.ID,
            NotesContract.Columns.TITLE,
            NotesContract.Columns.DESCRIPTION,
    };

    private static final long ID = 11L;
    private static final long ANOTHER_ID = 12L;

    private static final String TITLE = "Note title";
    private static final String ANOTHER_TITLE = "Another title";

    private static final String DESCRIPTION = "Note description";

    private ContentValues note;
    private SQLiteDatabase database;

    public ContentProviderIntegrationTests() {
        super(DbContentProvider.class, DbContract.AUTHORITY);
    }

    @Override protected void setUp() throws Exception {
        super.setUp();
        setContext(InstrumentationRegistry.getTargetContext());

        database = getProvider().sqLiteOpenHelper().getWritableDatabase();

        note = new ContentValues();
        note.put(NotesContract.Columns.ID, ID);
        note.put(NotesContract.Columns.TITLE, TITLE);
        note.put(NotesContract.Columns.DESCRIPTION, DESCRIPTION);
    }

    public void testGetTypeOnNotes_shouldReturnCorrectContentType() {
        assertThat(getProvider().getType(NotesContract.notes()))
                .isEqualTo(NotesContract.CONTENT_TYPE_DIR);
    }

    public void testGetTypeOnNoteItem_shouldReturnCorrectContentType() {
        assertThat(getProvider().getType(NotesContract.notes(1L)))
                .isEqualTo(NotesContract.CONTENT_TYPE_ITEM);
    }

    public void testGetTypeOnUnknownUri_shouldThrowException() {
        try {
            getProvider().getType(Uri.withAppendedPath(DbContract.AUTHORITY_URI, "test_resource"));

            fail("Exception was expected, but nothing was thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException).isNotNull();
        }
    }

    public void testInsertOnUnknownUri_shouldThrowException() {
        try {
            getProvider().insert(Uri.withAppendedPath(
                    DbContract.AUTHORITY_URI, "test_resource"), new ContentValues());

            fail("Exception was expected, but nothing was thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException).isNotNull();
        }
    }

    public void testUpdateOnUnknownUri_shouldThrowException() {
        try {
            getProvider().update(Uri.withAppendedPath(DbContract.AUTHORITY_URI, "test_resource"),
                    new ContentValues(), null, null);

            fail("Exception was expected, but nothing was thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException).isNotNull();
        }
    }

    public void testDeleteOnUnknownUri_shouldThrowException() {
        try {
            getProvider().delete(Uri.withAppendedPath(
                    DbContract.AUTHORITY_URI, "test_resource"), null, null);

            fail("Exception was expected, but nothing was thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException).isNotNull();
        }
    }

    public void testQueryOnUnknownUri_shouldThrowException() {
        try {
            getProvider().query(Uri.withAppendedPath(DbContract.AUTHORITY_URI, "test_resource"),
                    PROJECTION, null, null, null);

            fail("Exception was expected, but nothing was thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException).isNotNull();
        }
    }

    public void testInsertOnNotes_shouldInsertRowInDatabase() {
        Uri itemUri = getProvider().insert(NotesContract.notes(), note);

        assertThat(ContentUris.parseId(itemUri)).isEqualTo(ID);
        assertThatCursor(database.query(NotesContract.TABLE_NAME, PROJECTION,
                null, null, null, null, null)).hasRow(ID, TITLE, DESCRIPTION).isExhausted();
    }

    public void testUpdateOnNotes_shouldUpdateRowInDatabase() {
        database.insert(NotesContract.TABLE_NAME, null, note);
        note.put(NotesContract.Columns.TITLE, ANOTHER_TITLE);

        getProvider().update(NotesContract.notes(), note, NotesContract.Columns.ID + " = ?", new String[]{
                String.valueOf(ID)
        });

        assertThatCursor(database.query(NotesContract.TABLE_NAME, PROJECTION,
                null, null, null, null, null)).hasRow(ID, ANOTHER_TITLE, DESCRIPTION).isExhausted();
    }

    public void testUpdateOnNotesByUriWithId_shouldUpdateRowInDatabase() {
        database.insert(NotesContract.TABLE_NAME, null, note);
        note.put(NotesContract.Columns.TITLE, ANOTHER_TITLE);

        getProvider().update(NotesContract.notes(ID), note, null, null);

        assertThatCursor(database.query(NotesContract.TABLE_NAME, PROJECTION,
                null, null, null, null, null)).hasRow(ID, ANOTHER_TITLE, DESCRIPTION).isExhausted();
    }

    public void testDeleteOnNotes_shouldDeleteRowInDatabase() {
        database.insert(NotesContract.TABLE_NAME, null, note);

        getProvider().delete(NotesContract.notes(), NotesContract.Columns.ID + " = ?", new String[]{
                String.valueOf(ID)
        });

        assertThatCursor(database.query(NotesContract.TABLE_NAME, PROJECTION,
                null, null, null, null, null)).isExhausted();
    }

    public void testDeleteOnNotesByUriWithId_shouldDeleteRowInDatabase() {
        database.insert(NotesContract.TABLE_NAME, null, note);
        note.put(NotesContract.Columns.TITLE, ANOTHER_TITLE);

        getProvider().delete(NotesContract.notes(ID), null, null);

        assertThatCursor(database.query(NotesContract.TABLE_NAME, PROJECTION,
                null, null, null, null, null)).isExhausted();
    }

    public void testQueryOnNotes_shouldReadRowsFromDatabase() {
        database.insert(NotesContract.TABLE_NAME, null, note);

        note.put(NotesContract.Columns.ID, ANOTHER_ID);
        note.put(NotesContract.Columns.TITLE, ANOTHER_TITLE);

        database.insert(NotesContract.TABLE_NAME, null, note);

        assertThatCursor(getProvider().query(NotesContract.notes(), PROJECTION, null, null, null))
                .hasRow(ID, TITLE, DESCRIPTION)
                .hasRow(ANOTHER_ID, ANOTHER_TITLE, DESCRIPTION)
                .isExhausted();
    }

    public void testQueryOnNotesByUriWithId_shouldReadRowFromDatabase() {
        database.insert(NotesContract.TABLE_NAME, null, note);

        note.put(NotesContract.Columns.ID, ANOTHER_ID);
        note.put(NotesContract.Columns.TITLE, ANOTHER_TITLE);

        database.insert(NotesContract.TABLE_NAME, null, note);

        assertThatCursor(getProvider().query(NotesContract.notes(ANOTHER_ID), PROJECTION, null, null, null))
                .hasRow(ANOTHER_ID, ANOTHER_TITLE, DESCRIPTION)
                .isExhausted();
    }
}
