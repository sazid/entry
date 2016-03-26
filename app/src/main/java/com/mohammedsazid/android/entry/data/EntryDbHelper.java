package com.mohammedsazid.android.entry.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EntryDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "humming_sky.db";
    private static final int DATABASE_VERSION = 1;

    public EntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        DbContract.EntryTable.onCreate(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DbContract.EntryTable.onUpgrade(db, oldVersion, newVersion);
    }
}

