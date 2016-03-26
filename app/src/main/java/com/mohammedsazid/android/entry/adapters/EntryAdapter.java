package com.mohammedsazid.android.entry.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohammedsazid.android.entry.Entry;
import com.mohammedsazid.android.entry.R;

import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
    List<Entry> mEntries;

    public EntryAdapter(List<Entry> entries) {
        this.mEntries = entries;
    }

    public void setData(List<Entry> entries) {
        this.mEntries = entries;
    }

    @Override
    public long getItemId(int position) {
        return mEntries.get(position).getId();
    }

    @Override
    public int getItemCount() {
        if (mEntries == null)
            return 0;

        return mEntries.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Entry entry = mEntries.get(position);

        holder.entryTextTextView.setText(entry.getEntryText());
        holder.entryDateTextView.setText(entry.getEntryDateStr());
        holder.entryTimeTextView.setText(entry.getEntryTimeStr());
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entries_item_layout, parent, false);

        return new EntryViewHolder(v);
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView entryTextTextView;
        TextView entryDateTextView;
        TextView entryTimeTextView;

        public EntryViewHolder(View itemView) {
            super(itemView);

            entryTextTextView = (TextView) itemView.findViewById(R.id.entryTextTextView);
            entryDateTextView = (TextView) itemView.findViewById(R.id.entryDateTextView);
            entryTimeTextView = (TextView) itemView.findViewById(R.id.entryTimeTextView);
        }
    }
}
