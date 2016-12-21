package io.paper.android.notes;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

class NotesRepositoryImpl implements NotesRepository {
    private static final String QUERY_STATEMENT = "SELECT " +
            Note.Columns.ID + "," +
            Note.Columns.TITLE + "," +
            Note.Columns.DESCRIPTION + " FROM " + Note.TABLE_NAME;
    private static final String QUERY_STATEMENT_BY_ID = QUERY_STATEMENT +
            " WHERE " + Note.Columns.ID + " = ?";
    private static final String INSERT_STATEMENT = "INSERT INTO " + Note.TABLE_NAME + "(" +
            Note.Columns.TITLE + ", " +
            Note.Columns.DESCRIPTION + ")" +
            " VALUES (?, ?);";
    private static final String UPDATE_TITLE_STATEMENT = "UPDATE " + Note.TABLE_NAME + " SET " +
            Note.Columns.TITLE + " = ?" + " WHERE " + Note.Columns.ID + " = ? " + ";";
    private static final String UPDATE_DESCRIPTION_STATEMENT = "UPDATE " + Note.TABLE_NAME + " SET " +
            Note.Columns.DESCRIPTION + " = ?" + " WHERE " + Note.Columns.ID + " = ? " + ";";
    private static final String DELETE_STATEMENT = "DELETE FROM " + Note.TABLE_NAME + ";";

    private final BriteDatabase briteDatabase;
    private final SQLiteStatement insertStatement;
    private final SQLiteStatement updateTitleStatement;
    private final SQLiteStatement updateDescriptionStatement;
    private final SQLiteStatement deleteStatement;

    public NotesRepositoryImpl(BriteDatabase briteDatabase) {
        this.briteDatabase = briteDatabase;
        this.insertStatement = briteDatabase.getWriteableDatabase()
                .compileStatement(INSERT_STATEMENT);
        this.updateTitleStatement = briteDatabase.getWriteableDatabase()
                .compileStatement(UPDATE_TITLE_STATEMENT);
        this.updateDescriptionStatement = briteDatabase.getWriteableDatabase()
                .compileStatement(UPDATE_DESCRIPTION_STATEMENT);
        this.deleteStatement = briteDatabase.getWriteableDatabase()
                .compileStatement(DELETE_STATEMENT);
    }

    @Override
    public Observable<Long> add(final String title, final String description) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override public Observable<Long> call() {
                insertStatement.clearBindings();

                insertStatement.bindString(1, title);
                insertStatement.bindString(2, description);

                return Observable.just(briteDatabase.executeInsert(
                        Note.TABLE_NAME, insertStatement));
            }
        });
    }

    @Override
    public Observable<Integer> putTitle(final Long noteId, final String title) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                updateTitleStatement.clearBindings();

                updateTitleStatement.bindString(1, title);
                updateTitleStatement.bindLong(2, noteId);

                return Observable.just(briteDatabase.executeUpdateDelete(
                        Note.TABLE_NAME, updateTitleStatement));
            }
        });
    }

    @Override
    public Observable<Integer> putDescription(final Long noteId, final String description) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                updateDescriptionStatement.clearBindings();

                updateDescriptionStatement.bindString(1, description);
                updateDescriptionStatement.bindLong(2, noteId);

                return Observable.just(briteDatabase.executeUpdateDelete(
                        Note.TABLE_NAME, updateDescriptionStatement));
            }
        });
    }

    @Override
    public Observable<List<Note>> list() {
        return briteDatabase.createQuery(Note.TABLE_NAME, QUERY_STATEMENT)
                .mapToList(new Func1<Cursor, Note>() {
                    @Override public Note call(Cursor cursor) {
                        return Note.create(cursor);
                    }
                });
    }

    @Override
    public Observable<Note> get(Long noteId) {
        return briteDatabase.createQuery(Note.TABLE_NAME,
                QUERY_STATEMENT_BY_ID, String.valueOf(noteId))
                .mapToList(new Func1<Cursor, Note>() {
                    @Override public Note call(Cursor cursor) {
                        return Note.create(cursor);
                    }
                })
                .flatMap(new Func1<List<Note>, Observable<Note>>() {
                    @Override public Observable<Note> call(List<Note> notes) {
                        return Observable.from(notes);
                    }
                })
                .take(1);
    }

    @Override
    public Observable<Integer> clear() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                return Observable.just(briteDatabase.executeUpdateDelete(
                        Note.TABLE_NAME, deleteStatement));
            }
        });
    }
}
