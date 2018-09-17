package com.kokutouda.dnote.dnote.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Notes;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter {

    private List<Notes> notes;

    public NotesAdapter(List<Notes> notes) {
        this.notes = notes;
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleText;
        public TextView mContentText;

        public NotesViewHolder(View item) {
            super(item);
            mTitleText = item.findViewById(R.id.text_title);
            mContentText = item.findViewById(R.id.text_content);
        }
    }


    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_main, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotesViewHolder viewHolder = (NotesViewHolder) holder;
        Notes note = notes.get(position);

        viewHolder.mTitleText.setText(note.title);
        viewHolder.mContentText.setText(note.content);
    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            return notes.size();
        }
        return 0;
    }
}
