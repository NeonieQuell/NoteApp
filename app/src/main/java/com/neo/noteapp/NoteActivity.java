package com.neo.noteapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {

    //Objects
    private DBHandler dbHandler;

    //UI components
    private EditText etTitle, etDescription;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //Set actionbar text color
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        dbHandler = new DBHandler(this);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);

        /*Case 1: Save note on DB,
        * Case 2: Update note on DB*/
        if (MainActivity.actionCode == 1) {
            saveNote();
        } else {
            Intent intent = getIntent();

            String id = intent.getStringExtra("ID");

            etTitle.setText(intent.getStringExtra("Title"));
            etDescription.setText(intent.getStringExtra("Description"));

            updateNote(id);
        }
    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm a");

        return simpleDateFormat.format(calendar.getTime());
    }

    private void saveNote() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbHandler.saveNote(etTitle.getText().toString().trim(), etDescription.getText().toString().trim(), getDateTime())) {
                    Toast.makeText(NoteActivity.this, "Note saved.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(NoteActivity.this, "Error saving note.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateNote(String id) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbHandler.updateNote(id, etTitle.getText().toString().trim(), etDescription.getText().toString().trim(), getDateTime()) != 0) {
                    Toast.makeText(NoteActivity.this, "Note updated.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(NoteActivity.this, "Failed to update note.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}