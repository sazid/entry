package com.mohammedsazid.android.entry;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.mohammedsazid.android.entry.data.DbContract;

import java.text.SimpleDateFormat;
import java.util.Locale;

@SuppressWarnings("unused")
public class Entry {
    public static final SimpleDateFormat entryDateSdf =
            new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
    public static final SimpleDateFormat entryTimeSdf =
            new SimpleDateFormat("h:ma", Locale.getDefault());

    private long mId;
    private String mEntryText;
    private long mEntryTime;

    public Entry(String entryText, long entryTime) {
        this.mId = -1;
        this.mEntryText = entryText;
        this.mEntryTime = entryTime;
    }

    public Entry(long id, String entryText, long entryTime) {
        this.mId = id;
        this.mEntryText = entryText;
        this.mEntryTime = entryTime;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getEntryText() {
        return mEntryText;
    }

    public void setEntryText(String entryText) {
        this.mEntryText = entryText;
    }

    public long getEntryTime() {
        return this.mEntryTime;
    }

    public void setEntryTime(long entryTime) {
        this.mEntryTime = entryTime;
    }

    public String getEntryDateStr() {
        return entryDateSdf.format(mEntryTime);
    }

    public String getEntryTimeStr() {
        String s = entryTimeSdf.format(mEntryTime);
        s = s.replace("M", "");
        s = s.replace("A", "a");
        s = s.replace("P", "p");
        return s;
    }

    /**
     * Saves the item into the db
     *
     * @return Uri for the saved data
     * @throws Exception
     */
    public Uri save(ContentResolver cr) {
        ContentValues values = new ContentValues();
        values.put(DbContract.EntryTable.COL_TXT_ENTRY_TEXT, getEntryText());
        values.put(DbContract.EntryTable.COL_INT_ENTRY_TIME, getEntryTime());

        return cr.insert(
                DbContract.EntryTable.CONTENT_URI,
                values
        );
    }

    /**
     * Deletes the item from the db
     *
     * @return Number of rows deleted
     * @throws Exception
     */
    public int delete(ContentResolver cr) {
        if (getId() == -1) {
            return -1;
        }

        return cr.delete(
                DbContract.EntryTable.getUriForId(getId()),
                null,
                null
        );
    }
}
