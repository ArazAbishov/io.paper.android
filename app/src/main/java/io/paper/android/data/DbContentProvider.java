package io.paper.android.data;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;

import io.paper.android.notes.NotesContract;

import static android.content.ContentUris.parseId;
import static android.content.ContentUris.withAppendedId;
import static android.text.TextUtils.isEmpty;

public final class DbContentProvider extends ContentProvider {
    // matching items and item to constants
    private static final int NOTES = 10;
    private static final int NOTES_ID = 11;

    // android persistence apis
    private UriMatcher uriMatcher;
    private SQLiteOpenHelper databaseOpenHelper;
    private ContentResolver contentResolver;

    @Override
    public boolean onCreate() {
        if (getContext() == null) {
            throw new IllegalStateException();
        }

        databaseOpenHelper = new DbOpenHelper(getContext());
        contentResolver = getContext().getContentResolver();

        // building uri uriMatcher
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DbContract.AUTHORITY, NotesContract.RESOURCE_NOTES, NOTES);
        uriMatcher.addURI(DbContract.AUTHORITY, NotesContract.RESOURCE_NOTES_ID, NOTES_ID);

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NOTES: {
                return NotesContract.CONTENT_TYPE_DIR;
            }
            case NOTES_ID: {
                return NotesContract.CONTENT_TYPE_ITEM;
            }
            default: {
                throw new IllegalArgumentException("Unknown uri " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
            String[] args, String order) {
        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case NOTES: {
                Cursor cursor = database.query(NotesContract.TABLE_NAME, projection,
                        selection, args, null, null, order);
                cursor.setNotificationUri(contentResolver, uri);

                return cursor;
            }
            case NOTES_ID: {
                SQLiteQueryBuilder query = new SQLiteQueryBuilder();
                query.setTables(NotesContract.TABLE_NAME);
                query.appendWhere(BaseColumns._ID + " = " + parseId(uri));

                Cursor cursor = query.query(database, projection,
                        selection, args, null, null, order);
                cursor.setNotificationUri(contentResolver, uri);

                return cursor;
            }
            default: {
                throw new IllegalArgumentException("Unsupported uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case NOTES: {
                long databaseId = database.insertWithOnConflict(NotesContract.TABLE_NAME,
                        null, values, SQLiteDatabase.CONFLICT_REPLACE);

                // notify listeners of URI about new row
                contentResolver.notifyChange(uri, null);

                // append row id and return it back
                return withAppendedId(uri, databaseId);
            }
            default: {
                throw new IllegalArgumentException("Unsupported uri: " + uri);
            }
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String query, String[] args) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case NOTES: {
                int updatedRows = database.update(NotesContract.TABLE_NAME, values, query, args);

                // notify listeners of URI about updates in the table
                contentResolver.notifyChange(uri, null);

                return updatedRows;
            }
            case NOTES_ID: {
                String where = BaseColumns._ID + " = " + parseId(uri);
                if (!isEmpty(query)) {
                    where = where + " AND " + query;
                }

                int updatedRows = database.update(NotesContract.TABLE_NAME, values, where, args);

                // notify listeners of URI about updates in the table
                contentResolver.notifyChange(uri, null);

                return updatedRows;
            }
            default: {
                throw new IllegalArgumentException("Unsupported uri: " + uri);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String query, String[] args) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case NOTES: {
                int deletedRows = database.delete(NotesContract.TABLE_NAME, query, args);

                // notify listeners of URI about deletes in the table
                contentResolver.notifyChange(uri, null);

                return deletedRows;
            }
            case NOTES_ID: {
                String where = BaseColumns._ID + " = " + parseId(uri);
                if (!isEmpty(query)) {
                    where = where + " AND " + query;
                }

                int deletedRows = database.delete(NotesContract.TABLE_NAME, where, args);

                // notify listeners of URI about deletes in the table
                contentResolver.notifyChange(uri, null);

                return deletedRows;
            }
            default: {
                throw new IllegalArgumentException("Unsupported uri: " + uri);
            }
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> ops) throws OperationApplicationException {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        try {
            database.beginTransaction();

            ContentProviderResult[] results = new ContentProviderResult[ops.size()];
            for (int index = 0; index < ops.size(); index++) {
                results[index] = ops.get(index).apply(this, results, index);
            }

            database.setTransactionSuccessful();
            return results;
        } finally {
            database.endTransaction();
        }
    }

    @VisibleForTesting
    SQLiteOpenHelper sqLiteOpenHelper() {
        return databaseOpenHelper;
    }
}
