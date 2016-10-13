package io.paper.android.stores;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentUris.parseId;
import static android.content.ContentUris.withAppendedId;
import static android.text.TextUtils.isEmpty;

public final class DbContentProvider extends ContentProvider {
    private static final int PATH_SEGMENT_TABLE = 0;

    // matching items and item to constants
    private static final int ITEMS = 1;
    private static final int ITEMS_ID = 2;

    // contract related fields
    private UriMatcher matcher;
    private List<Contract> contracts;

    // android persistence apis
    private SQLiteOpenHelper databaseOpenHelper;
    private ContentResolver contentResolver;

    @Override
    public boolean onCreate() {
        if (getContext() == null) {
            throw new IllegalStateException();
        }

        databaseOpenHelper = new DbOpenHelper(getContext());
        contentResolver = getContext().getContentResolver();

        contracts = new ArrayList<>();
        contracts.add(new NotesContract());

        // building uri matcher
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (Contract contract : contracts) {
            String items = contract.table();
            String itemId = String.format(Locale.US, "%s/#/", contract.table());

            matcher.addURI(DbContract.AUTHORITY, items, ITEMS);
            matcher.addURI(DbContract.AUTHORITY, itemId, ITEMS_ID);
        }

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Contract contract = findMatchingContract(uri);
        switch (matcher.match(uri)) {
            case ITEMS:
                return String.format(Locale.US, "%s/vnd.%s.%s",
                        ContentResolver.CURSOR_DIR_BASE_TYPE, DbContract.AUTHORITY, contract.table());
            case ITEMS_ID:
                return String.format(Locale.US, "%s/vnd.%s.%s",
                        ContentResolver.CURSOR_ITEM_BASE_TYPE, DbContract.AUTHORITY, contract.table());
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] args, String order) {
        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();
        Contract contract = findMatchingContract(uri);

        switch (matcher.match(uri)) {
            case ITEMS: {
                Cursor cursor = database.query(contract.table(), projection,
                        selection, args, null, null, order);
                cursor.setNotificationUri(contentResolver, uri);

                return cursor;
            }
            case ITEMS_ID: {
                SQLiteQueryBuilder query = new SQLiteQueryBuilder();
                query.setTables(contract.table());
                query.appendWhere(BaseColumns._ID + " = " + parseId(uri));

                Cursor cursor = query.query(database, projection,
                        selection, args, null, null, order);
                cursor.setNotificationUri(contentResolver, uri);

                return cursor;
            }
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        Contract contract = findMatchingContract(uri);

        switch (matcher.match(uri)) {
            case ITEMS: {
                long databaseId = database.insertWithOnConflict(contract.table(),
                        null, values, SQLiteDatabase.CONFLICT_REPLACE);

                // notify listeners of URI about new row
                contentResolver.notifyChange(uri, null);

                // append row id and return it back
                return withAppendedId(uri, databaseId);
            }
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String query, String[] args) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        Contract contract = findMatchingContract(uri);

        switch (matcher.match(uri)) {
            case ITEMS: {
                int updatedRows = database.update(contract.table(), values, query, args);

                // notify listeners of URI about updates in the table
                contentResolver.notifyChange(uri, null);

                return updatedRows;
            }
            case ITEMS_ID: {
                String where = BaseColumns._ID + " = " + parseId(uri);
                if (!isEmpty(query)) {
                    where = where + " AND " + query;
                }

                int updatedRows = database.update(contract.table(), values, where, args);

                // notify listeners of URI about updates in the table
                contentResolver.notifyChange(uri, null);

                return updatedRows;
            }
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String query, String[] args) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        Contract contract = findMatchingContract(uri);

        switch (matcher.match(uri)) {
            case ITEMS: {
                int deletedRows = database.delete(contract.table(), query, args);

                // notify listeners of URI about deletes in the table
                contentResolver.notifyChange(uri, null);

                return deletedRows;
            }
            case ITEMS_ID: {
                String where = BaseColumns._ID + " = " + parseId(uri);
                if (!isEmpty(query)) {
                    where = where + " AND " + query;
                }

                int deletedRows = database.delete(contract.table(), where, args);

                // notify listeners of URI about deletes in the table
                contentResolver.notifyChange(uri, null);

                return deletedRows;
            }
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
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

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        Contract contract = findMatchingContract(uri);

        try {
            database.beginTransaction();

            for (ContentValues value : values) {
                database.insert(contract.table(), null, value);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        contentResolver.notifyChange(uri, null);
        database.close();

        return values.length;
    }

    private Contract findMatchingContract(Uri uri) {
        for (Contract contract : contracts) {
            String table = uri.getPathSegments().get(PATH_SEGMENT_TABLE);
            if (contract.table().equals(table)) {
                return contract;
            }
        }

        throw new IllegalArgumentException("No corresponding contract found for given uri: " + uri);
    }
}
