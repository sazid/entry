package com.mohammedsazid.android.entry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mohammedsazid.android.entry.adapters.EntryAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Entry> mEntries;

    EntryAdapter mEntryAdapter;
    RecyclerView mEntryRv;
    EditText mEntryEt;
    ImageButton mEntryBtn;

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
        Entry entry = new Entry(
                mEntries.size() - 1,
                mEntryEt.getText().toString(),
                System.currentTimeMillis()
        );
        mEntries.add(0, entry);
        mEntryAdapter.notifyDataSetChanged();
        mEntryEt.setText("");
    }

    private void loadData() {
        mEntries = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Entry entry = new Entry(
                    i,
                    "Sample text " + i + ". This is a really long long text. Another simple test." +
                            "This is same. Another " + i + " test. " + System.currentTimeMillis(),
                    System.currentTimeMillis());
            mEntries.add(entry);
        }

        initAdapter(mEntries);
    }

    private void initAdapter(List<Entry> entries) {
        if (entries != null) {
            mEntryAdapter.setData(entries);
            mEntryAdapter.notifyDataSetChanged();
            return;
        }

        mEntryAdapter = new EntryAdapter(entries);
        mEntryAdapter.setHasStableIds(true);
        mEntryRv.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, true));
        mEntryRv.setHasFixedSize(true);
        mEntryRv.setAdapter(this.mEntryAdapter);
    }
}
