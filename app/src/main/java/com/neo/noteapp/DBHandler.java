package com.neo.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    //DB Schema
    private static final String DATABASE_NAME = "NoteApp.db";
    private static final String TABLE_NAME = "Notes";
    private static final String COL1 = "ID";
    private static final String COL2 = "Title";
    private static final String COL3 = "Description";
    private static final String COL4 = "DateTime";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //DB column getters
    public static String getCOL1() {
        return COL1;
    }

    public static String getCOL2() {
        return COL2;
    }

    public static String getCOL3() {
        return COL3;
    }

    public static String getCOL4() {
        return COL4;
    }

    //Save to DB
    public boolean saveNote(String title, String description, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL2, title);
        cv.put(COL3, description);
        cv.put(COL4, dateTime);

        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    //Update note on DB
    public int updateNote(String id, String title, String description, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL2, title);
        cv.put(COL3, description);
        cv.put(COL4, dateTime);

        return db.update(TABLE_NAME, cv, COL1 + " = ?", new String[] { id });
    }

    //Delete note on DB
    public boolean deleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL1 + " = ?", new String[] { id }) != 0;
    }

    //Get all DB data
    public Cursor getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}