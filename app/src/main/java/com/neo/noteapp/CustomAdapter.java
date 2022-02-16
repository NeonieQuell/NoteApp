package com.neo.noteapp;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.NoteViewHolder> {

    //Objects
    private ArrayList<NoteItem> noteList;

    //OnItemClickListener
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //ViewHolder class
    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        //UI components
        public TextView txtTitle, txtDescription, txtDateTime;

        public NoteViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    //Constructor
    public CustomAdapter(ArrayList<NoteItem> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_layout, viewGroup, false);
        return new NoteViewHolder(view, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
        NoteItem currentItem = noteList.get(position);
        noteViewHolder.txtTitle.setText(currentItem.getTitle());
        noteViewHolder.txtDescription.setText(currentItem.getDescription());
        noteViewHolder.txtDateTime.setText("Last Modified: " + currentItem.getDateTime());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}