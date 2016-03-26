package com.mohammedsazid.android.entry.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    public class EntryViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {
        TextView entryTextTextView;
        TextView entryDateTextView;
        TextView entryTimeTextView;

        public EntryViewHolder(View itemView) {
            super(itemView);

            entryTextTextView = (TextView) itemView.findViewById(R.id.entryTextTextView);
            entryDateTextView = (TextView) itemView.findViewById(R.id.entryDateTextView);
            entryTimeTextView = (TextView) itemView.findViewById(R.id.entryTimeTextView);

            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(final View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Actions")
                    .setItems(new String[]{"Copy", "Delete"}, new EntryActionsDialog(v.getContext()));
            builder.show();

            return true;
        }

        private class EntryActionsDialog implements DialogInterface.OnClickListener {
            Context context;

            public EntryActionsDialog(Context context) {
                this.context = context;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Entry entry = EntryAdapter.this.mEntries.get(getAdapterPosition());

                switch (which) {
                    case 0:
                        ClipboardManager clipboard = (ClipboardManager)
                                context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(entry.getEntryText(), entry.getEntryText());
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        new AlertDialog.Builder(context)
                                .setTitle("Confirm delete")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Thread t = new Thread() {
                                            @Override
                                            public void run() {
                                                entry.delete(context.getContentResolver());
                                            }
                                        };
                                        t.start();
                                        mEntries.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                    }
                                })
                                .setNegativeButton("NO", null)
                                .show();
                        break;
                }
            }
        }
    }
}
