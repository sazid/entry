package com.mohammedsazid.android.entry;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mohammedsazid.android.entry.adapters.EntryAdapter;
import com.mohammedsazid.android.entry.data.DbContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Entry> mEntries;
    final Handler mHandler = new Handler();

    EntryAdapter mEntryAdapter;
    RecyclerView mEntryRv;
    EditText mEntryEt;
    ImageButton mEntryBtn;

    EntryLoadTask mLoadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        bindListeners();
        initAdapter(null);
        loadData();
    }

    private void bindViews() {
        mEntryRv = (RecyclerView) findViewById(R.id.entryRecyclerView);
        mEntryEt = (EditText) findViewById(R.id.entryEditText);
        mEntryBtn = (ImageButton) findViewById(R.id.entryButton);
    }

    private void bindListeners() {
        if (mEntryEt.getText().length() == 0) {
            mEntryBtn.setEnabled(false);
        }

        mEntryEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mEntryBtn.setAlpha(0.3f);
                    mEntryBtn.setEnabled(false);
                } else {
                    mEntryBtn.setAlpha(1f);
                    mEntryBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertEntry();
            }
        });
    }

    private void insertEntry() {
        final Entry entry = new Entry(
                mEntryEt.getText().toString(),
                System.currentTimeMillis()
        );
        mEntryEt.setText("");

        Thread t = new Thread() {
            @Override
            public void run() {
                Uri insertUri = entry.save(MainActivity.this.getContentResolver());
                entry.setId(ContentUris.parseId(insertUri));
                mEntries.add(0, entry);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mEntryAdapter.notifyDataSetChanged();
                    }
                });

            }
        };
        t.start();
    }

    private void loadData() {
        mLoadTask = new EntryLoadTask();
        mLoadTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoadTask.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mEntries == null) {
            if (mLoadTask == null || mLoadTask.isCancelled()) {
                mLoadTask = new EntryLoadTask();
                mLoadTask.execute();
            }
        }
    }

    private void initAdapter(List<Entry> entries) {
        if (entries != null) {
            mEntryAdapter.setData(entries);
            mEntryAdapter.notifyDataSetChanged();
            return;
        }

        mEntryAdapter = new EntryAdapter(null);
        mEntryAdapter.setHasStableIds(true);
        mEntryRv.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, true));
        mEntryRv.setHasFixedSize(true);
        mEntryRv.setAdapter(this.mEntryAdapter);
    }

    private class EntryLoadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
            MainActivity.this.initAdapter(MainActivity.this.mEntries);
        }

        @Override
        protected Void doInBackground(Void... params) {
            load();
            return null;
        }

        private void load() {
            MainActivity.this.mEntries = new ArrayList<>();

            ContentResolver cr = MainActivity.this.getContentResolver();
            long entryId = -1;
            String entryText = "";
            long entryTime = 0;

            Cursor c = cr.query(
                    DbContract.EntryTable.CONTENT_URI,
                    new String[]{
                            DbContract.EntryTable._ID,
                            DbContract.EntryTable.COL_TXT_ENTRY_TEXT,
                            DbContract.EntryTable.COL_INT_ENTRY_TIME
                    },
                    null,
                    null,
                    null
            );

            if (c != null && c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    entryId = c.getLong(c.getColumnIndex(DbContract.EntryTable._ID));
                    entryText = c.getString(c.getColumnIndex(DbContract.EntryTable.COL_TXT_ENTRY_TEXT));
                    entryTime = c.getLong(c.getColumnIndex(DbContract.EntryTable.COL_INT_ENTRY_TIME));

                    Entry entry = new Entry(entryId, entryText, entryTime);
                    MainActivity.this.mEntries.add(entry);

                    c.moveToNext();
                }

                if (!c.isClosed()) {
                    c.close();
                }
            }
        }
    }
}
