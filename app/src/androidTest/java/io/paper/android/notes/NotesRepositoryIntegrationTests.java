package io.paper.android.notes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.paper.android.PaperApp;
import io.paper.android.data.DbOpenHelper;
import rx.schedulers.Schedulers;

import static com.google.common.truth.Truth.assertThat;
import static io.paper.android.data.CursorAssert.assertThatCursor;


@RunWith(AndroidJUnit4.class)
public class NotesRepositoryIntegrationTests {
    private static final String[] PROJECTION = {
            Note.Columns.ID,
            Note.Columns.TITLE,
            Note.Columns.DESCRIPTION
    };

    private SQLiteDatabase database;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private NotesRepository notesRepository;

    @Before
    public void setUp() {
        SqlBrite sqlBrite = PaperApp.getAppComponent(InstrumentationRegistry
                .getTargetContext()).sqlBrite();
        sqLiteOpenHelper = new DbOpenHelper(
                InstrumentationRegistry.getTargetContext(), null);

        // setting immediate scheduler in order to have synchronous behaviour
        BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(
                sqLiteOpenHelper, Schedulers.immediate());

        database = sqLiteOpenHelper.getWritableDatabase();
        notesRepository = new NotesRepositoryImpl(briteDatabase);
    }

    @Test
    public void add_shouldPersistNoteInDatabase() {
        Long noteId = notesRepository.add(
                "test_note_title",
                "test_note_description"
        ).toBlocking().first();

        Cursor cursor = database.query(Note.TABLE_NAME, PROJECTION,
                null, null, null, null, null);

        assertThat(noteId).isEqualTo(1L);
        assertThatCursor(cursor)
                .hasRow(1L, "test_note_title", "test_note_description")
                .isExhausted();
    }

    @Test
    public void putTitle_shouldUpdateExistingRowInDatabase() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Note.Columns.ID, 10L);
        contentValues.put(Note.Columns.TITLE, "test_note_title");
        contentValues.put(Note.Columns.DESCRIPTION, "test_note_description");

        database.insert(Note.TABLE_NAME, null, contentValues);

        Integer updatedRows = notesRepository.putTitle(
                10L, "test_new_note_title"
        ).toBlocking().first();

        Cursor cursor = database.query(Note.TABLE_NAME, PROJECTION,
                null, null, null, null, null);

        assertThat(updatedRows).isEqualTo(1);
        assertThatCursor(cursor)
                .hasRow(10L, "test_new_note_title", "test_note_description")
                .isExhausted();
    }

    @Test
    public void putDescription_shouldUpdateExistingRowInDatabase() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Note.Columns.ID, 10L);
        contentValues.put(Note.Columns.TITLE, "test_note_title");
        contentValues.put(Note.Columns.DESCRIPTION, "test_note_description");

        database.insert(Note.TABLE_NAME, null, contentValues);

        Integer updatedRows = notesRepository.putDescription(
                10L, "test_new_note_description"
        ).toBlocking().first();

        Cursor cursor = database.query(Note.TABLE_NAME, PROJECTION,
                null, null, null, null, null);

        assertThat(updatedRows).isEqualTo(1);
        assertThatCursor(cursor)
                .hasRow(10L, "test_note_title", "test_new_note_description")
                .isExhausted();
    }

    @Test
    public void list_shouldReturnPersistedNotes() {
        Note note = Note.builder()
                .id(10L)
                .title("test_note_title")
                .description("test_note_description")
                .build();

        database.insert(Note.TABLE_NAME, null, note.toContentValues());

        Note persistedNote = notesRepository.list()
                .toBlocking().first().get(0);

        assertThat(persistedNote.id()).isEqualTo(10L);
        assertThat(persistedNote.title()).isEqualTo("test_note_title");
        assertThat(persistedNote.description()).isEqualTo("test_note_description");
    }

    @Test
    public void get_shouldReturnPersistedNote() {
        Note note = Note.builder()
                .id(10L)
                .title("test_note_title")
                .description("test_note_description")
                .build();

        database.insert(Note.TABLE_NAME, null, note.toContentValues());

        Note persistedNote = notesRepository.get(10L)
                .toBlocking().first();

        assertThat(persistedNote.id()).isEqualTo(10L);
        assertThat(persistedNote.title()).isEqualTo("test_note_title");
        assertThat(persistedNote.description()).isEqualTo("test_note_description");
    }

    @Test
    public void clear_shouldDeleteAllPersistedRows() {
        Note note = Note.builder()
                .id(10L)
                .title("test_note_title")
                .description("test_note_description")
                .build();

        database.insert(Note.TABLE_NAME, null, note.toContentValues());

        Integer deletedRows = notesRepository.clear().toBlocking().first();

        Cursor cursor = database.query(Note.TABLE_NAME, PROJECTION,
                null, null, null, null, null);

        assertThat(deletedRows).isEqualTo(1);
        assertThatCursor(cursor).isExhausted();
    }

    @After
    public void tearDown() {
        // erase contents of in-memory database
        // and close connection to it
        sqLiteOpenHelper.close();
    }
}
