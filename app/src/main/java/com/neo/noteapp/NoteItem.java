package com.neo.noteapp;

public class NoteItem {

    //Variables
    private final String id, title, description, dateTime;

    //Constructor
    public NoteItem(String id, String title, String description, String dateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateTime() {
        return dateTime;
    }
}