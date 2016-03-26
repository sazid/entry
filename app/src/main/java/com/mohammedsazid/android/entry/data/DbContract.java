package com.mohammedsazid.android.entry.data;

import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {
    // The content authority is the name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.mohammedsazid.android.entry.data";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ENTRY = "entry";

    public static class EntryTable implements BaseColumns {
        // Content uri for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRY).build();

        // Data type constants used by the content provider
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ENTRY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ENTRY;

        // Name of the table
        public static final String TABLE_NAME = "entry";

        // Column names
        public static final String COL_TXT_ENTRY_TEXT = "entry_text";
        public static final String COL_INT_ENTRY_TIME = "entry_time";

        // SQL query for creating the table
        public static final String SQL_Q_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME
                        + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_TXT_ENTRY_TEXT + " TEXT NOT NULL, "
                        + COL_INT_ENTRY_TIME + " INTEGER NOT NULL"
                        + ")"
                        + ";";

        // Creates the table
        public static void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_Q_CREATE_TABLE);
        }

        // Upgrades the table based on the given version number
        public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // For now, let's just the drop the table and create a completely blank new one
            // WARNING: *** Do not drop the db (users will get angry :v) ***
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            onCreate(db);
        }

        /**
         * Builds the uri for a given <code>id</code>
         *
         * @param id The <code>id</code> of the uri to be built
         * @return <code>uri</code> with the appended <code>id</code>
         */
        public static Uri getUriForId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
