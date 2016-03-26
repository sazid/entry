/*
 * Copyright (c) 2015  Mohammed Sazid Al Rashid All Rights Reserved
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package com.mohammedsazid.android.entry.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;

public class EntryProvider extends ContentProvider {
    // int constants for identifying a Uri
    private static final int ENTRY = 100;
    private static final int ENTRY_ID = 101;

    // UriMatcher for matching different uris
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Used for getting access to the database
    private EntryDbHelper mDbOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DbContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, DbContract.PATH_ENTRY, ENTRY);
        uriMatcher.addURI(authority, DbContract.PATH_ENTRY + "/#", ENTRY_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbOpenHelper = new EntryDbHelper(getContext());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cursor query(
            @NonNull Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        final SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        Cursor cursor;
        String _id;

        switch (sUriMatcher.match(uri)) {
            case ENTRY:
                if (sortOrder == null)
                    sortOrder = DbContract.EntryTable.COL_INT_ENTRY_TIME + " DESC";
                cursor = db.query(
                        DbContract.EntryTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ENTRY_ID:
                _id = uri.getLastPathSegment();
                if (sortOrder == null)
                    sortOrder = DbContract.EntryTable.COL_INT_ENTRY_TIME + " DESC";
                cursor = db.query(
                        DbContract.EntryTable.TABLE_NAME,
                        projection,
                        DbContract.EntryTable.TABLE_NAME + "." + DbContract.EntryTable._ID + " = ?",
                        new String[]{_id},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        return cursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ENTRY:
                return DbContract.EntryTable.CONTENT_TYPE;
            case ENTRY_ID:
                return DbContract.EntryTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ENTRY: {
                long _id = db.insert(
                        DbContract.EntryTable.TABLE_NAME,
                        null,
                        values
                );
                if (_id > -1)
                    returnUri = DbContract.EntryTable.getUriForId(_id);
                else
                    throw new SQLiteException("Failed to insert row: " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        int deleteCount;
        String _id;

        switch (sUriMatcher.match(uri)) {
            case ENTRY:
                deleteCount = db.delete(DbContract.EntryTable.TABLE_NAME, selection, selectionArgs);
                break;
            case ENTRY_ID:
                _id = uri.getLastPathSegment();
                deleteCount = db.delete(
                        DbContract.EntryTable.TABLE_NAME,
                        DbContract.EntryTable.TABLE_NAME + "." + DbContract.EntryTable._ID + " = ?",
                        new String[]{_id}
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        int updateCount;
        String _id;

        switch (sUriMatcher.match(uri)) {
            case ENTRY:
                updateCount = db.update(
                        DbContract.EntryTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case ENTRY_ID:
                _id = uri.getLastPathSegment();
                updateCount = db.update(
                        DbContract.EntryTable.TABLE_NAME,
                        values,
                        DbContract.EntryTable.TABLE_NAME + "." + DbContract.EntryTable._ID + " = ?",
                        new String[]{_id}
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
