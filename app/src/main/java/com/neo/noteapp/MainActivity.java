package com.neo.noteapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Objects
    private ArrayList<NoteItem> noteList;
    private CustomAdapter adapter;
    private DBHandler dbHandler;

    //Variables
    public static int actionCode; //Variable to determine if the action to perform on DB is INSERT or UPDATE

    //UI components
    private LinearLayout layoutNoDataToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set actionbar text color
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        //Initialize object
        noteList = new ArrayList<>();
        dbHandler = new DBHandler(this);

        layoutNoDataToDisplay = findViewById(R.id.layoutNoDataToDisplay);

        //Invoke methods
        checkLoad();
        buildRecyclerView();
        createNote();
        loadNoteList();
    }

    //Build the recyclerView
    private void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new CustomAdapter(noteList);

        //RecyclerView swipe action
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //Set recyclerView onClick
        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                loadNoteItem(position);
            }
        });
    }

    //Check if DB contains data
    private void checkLoad() {
        if (dbHandler.getNotes().getCount() == 0) {
            layoutNoDataToDisplay.setVisibility(View.VISIBLE);
        } else {
            layoutNoDataToDisplay.setVisibility(View.INVISIBLE);
        }
    }

    //Create a new note
    private void createNote() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                actionCode = 1;
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @SuppressLint("Range")
    private void loadNoteList() {
        if (layoutNoDataToDisplay.getVisibility() == View.INVISIBLE) {
            Cursor cursor = dbHandler.getNotes();

            while (cursor.moveToNext()) {
                noteList.add(new NoteItem(
                        cursor.getString(cursor.getColumnIndex(DBHandler.getCOL1())),
                        cursor.getString(cursor.getColumnIndex(DBHandler.getCOL2())),
                        cursor.getString(cursor.getColumnIndex(DBHandler.getCOL3())),
                        cursor.getString(cursor.getColumnIndex(DBHandler.getCOL4()))
                ));
            }
        }
    }

    /*Whenever the user clicks an item on recyclerView,
    * Load the item content on NoteActivity layout*/
    private void loadNoteItem(int position) {
        actionCode = 2;
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("ID", noteList.get(position).getId());
        intent.putExtra("Title", noteList.get(position).getTitle());
        intent.putExtra("Description", noteList.get(position).getDescription());
        startActivityForResult(intent, 1);
    }

    //After INSERT or UPDATE of a note, recreate the parent activity to refresh contents
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
                this.recreate();
        }
    }

    //Swipe action for recyclerview
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int position = viewHolder.getAdapterPosition();
            NoteItem noteObj = noteList.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirm")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton(Html.fromHtml("<font color=\"black\">" + "YES" + "</font>")
                            , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (dbHandler.deleteNote(noteObj.getId())) {
                                noteList.remove(position);
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(MainActivity.this, "Item deleted.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Item not deleted.", Toast.LENGTH_SHORT).show();
                            }

                            checkLoad();
                        }
                    })
                    .setNegativeButton(Html.fromHtml("<font color=\"black\">" + "NO" + "</font>"),
                            new DialogInterface.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Item not deleted.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    };
}