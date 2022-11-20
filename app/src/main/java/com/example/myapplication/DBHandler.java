package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "noteDB";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "notes";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "name";
    private static final String KEY_CREATE_DATE = "address";
    private static final String KEY_CONTENT = "phone_number";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_Notes_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_TITLE, KEY_CREATE_DATE, KEY_CONTENT);
        db.execSQL(create_Notes_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_Notes_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_Notes_table);
        onCreate(db);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CREATE_DATE, note.getCreateDate());
        values.put(KEY_CONTENT, note.getContent());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> NotesList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Note note = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            NotesList.add(note);
            cursor.moveToNext();
        }
        return NotesList;
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = " + noteId, null);
        db.close();
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CREATE_DATE, note.getCreateDate());
        values.put(KEY_CONTENT, note.getContent());

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(note.getId()) });
        db.close();
    }
}
