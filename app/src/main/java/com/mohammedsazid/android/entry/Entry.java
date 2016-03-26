package com.mohammedsazid.android.entry;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Entry {
    public static final SimpleDateFormat entryDateSdf =
            new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
    public static final SimpleDateFormat entryTimeSdf =
            new SimpleDateFormat("h:ma", Locale.getDefault());

    private long mId;
    private String mEntryText;
    private long mEntryTime;

    public Entry(String entryText, long entryTime) {
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
     * @throws Exception
     */
    public void save() throws Exception {

    }

    /**
     * Deletes the item from the db
     *
     * @throws Exception
     */
    public void delete() throws Exception {

    }
}
