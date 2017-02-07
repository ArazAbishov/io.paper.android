package io.paper.android.notes;

import android.database.sqlite.SQLiteStatement;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.paper.android.notes.Note.Columns;
import io.reactivex.Observable;

class NotesRepositoryImpl implements NotesRepository {
    private static final String QUERY_STATEMENT = "SELECT " +
            Columns.ID + "," +
            Columns.TITLE + "," +
            Columns.DESCRIPTION + " FROM " + Note.TABLE_NAME;
    private static final String QUERY_STATEMENT_BY_ID = QUERY_STATEMENT +
            " WHERE " + Columns.ID + " = ?";
    private static final String INSERT_STATEMENT = "INSERT INTO " + Note.TABLE_NAME + "(" +
            Columns.TITLE + ", " +
            Columns.DESCRIPTION + ")" +
            " VALUES (?, ?);";
    private static final String UPDATE_TITLE_STATEMENT = "UPDATE " + Note.TABLE_NAME +
            " SET " + Columns.TITLE + " = ?" + " WHERE " + Columns.ID + " = ? " + ";";
    private static final String UPDATE_DESCRIPTION_STATEMENT = "UPDATE " + Note.TABLE_NAME +
            " SET " + Columns.DESCRIPTION + " = ?" + " WHERE " + Columns.ID + " = ? " + ";";
    private static final String DELETE_STATEMENT = "DELETE FROM " + Note.TABLE_NAME + ";";

    private final BriteDatabase briteDatabase;
    private final SQLiteStatement insertStatement;
    private final SQLiteStatement updateTitleStatement;
    private final SQLiteStatement updateDescriptionStatement;
    private final SQLiteStatement deleteStatement;

    public NotesRepositoryImpl(BriteDatabase briteDatabase) {
        this.briteDatabase = briteDatabase;
        this.insertStatement = briteDatabase.getWritableDatabase()
                .compileStatement(INSERT_STATEMENT);
        this.updateTitleStatement = briteDatabase.getWritableDatabase()
                .compileStatement(UPDATE_TITLE_STATEMENT);
        this.updateDescriptionStatement = briteDatabase.getWritableDatabase()
                .compileStatement(UPDATE_DESCRIPTION_STATEMENT);
        this.deleteStatement = briteDatabase.getWritableDatabase()
                .compileStatement(DELETE_STATEMENT);
    }

    @Override
    public Observable<Long> add(final String title, final String description) {
        return Observable.defer(() -> {
            insertStatement.clearBindings();

            insertStatement.bindString(1, title);
            insertStatement.bindString(2, description);

            return Observable.just(briteDatabase.executeInsert(
                    Note.TABLE_NAME, insertStatement));
        });
    }

    @Override
    public Observable<Integer> putTitle(final Long noteId, final String title) {
        return Observable.defer(() -> {
            updateTitleStatement.clearBindings();

            updateTitleStatement.bindString(1, title);
            updateTitleStatement.bindLong(2, noteId);

            return Observable.just(briteDatabase.executeUpdateDelete(
                    Note.TABLE_NAME, updateTitleStatement));
        });
    }

    @Override
    public Observable<Integer> putDescription(final Long noteId, final String description) {
        return Observable.defer(() -> {
            updateDescriptionStatement.clearBindings();

            updateDescriptionStatement.bindString(1, description);
            updateDescriptionStatement.bindLong(2, noteId);

            return Observable.just(briteDatabase.executeUpdateDelete(
                    Note.TABLE_NAME, updateDescriptionStatement));
        });
    }

    @Override
    public Observable<List<Note>> list() {
        return RxJavaInterop.toV2Observable(briteDatabase.createQuery(
                Note.TABLE_NAME, QUERY_STATEMENT)
                .mapToList(Note::create));
    }

    @Override
    public Observable<Note> get(Long noteId) {
        return RxJavaInterop.toV2Observable(briteDatabase.createQuery(Note.TABLE_NAME,
                QUERY_STATEMENT_BY_ID, String.valueOf(noteId))
                .mapToList(Note::create))
                .flatMap(Observable::fromIterable)
                .take(1);
    }

    @Override
    public Observable<Integer> clear() {
        return Observable.defer(() -> Observable.just(briteDatabase
                .executeUpdateDelete(Note.TABLE_NAME, deleteStatement))
        );
    }
}
