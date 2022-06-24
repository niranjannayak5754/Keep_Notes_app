package com.example.keepnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keepnotes.models.Notes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

//import android.text.BoringLayout;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String dbname = "my_database";
    private static final int version = 1;

    public DatabaseHelper(Context context) {
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE users_data ( Email TEXT PRIMARY KEY ,Name TEXT ," +
                " Password TEXT, Phone TEXT) ";
        String queryNote = "CREATE TABLE all_notes (id INTEGER PRIMARY KEY AUTOINCREMENT , user_id TEXT, Title TEXT ,Description TEXT" +
                ",Image1 TEXT,Image2 TEXT, Image3 TEXT, Image4 TEXT, Image5 TEXT, Image6 TEXT,Image7 TEXT, Image8 TEXT, Image9 TEXT, Image10 TEXT)";

        db.execSQL(query);
        db.execSQL(queryNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS users_data");
        db.execSQL("DROP TABLE IF EXISTS all_notes");
        onCreate(db);
    }

    public boolean insertUserData(String name, String email, String password, String phone) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues user = new ContentValues();

        user.put("Email", email);
        user.put("Name", name);
        user.put("Password", password);
        user.put("Phone", phone);

        long r = db.insert("users_data", null, user);
        return r != -1;
    }

    public boolean checkUser(String email) {

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from users_data where Email=?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

    public boolean checkEmailPassword(String email, String password) {

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from users_data where Email=? and Password=?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean addNote(@NotNull String userId, @NotNull String title, @NotNull String description, @NotNull ArrayList<String> imagesArray) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues note = new ContentValues();

        note.put("user_id", userId);
        note.put("Title", title);
        note.put("Description", description);

        for (int i = 0; i < imagesArray.size(); i++) {
            String idImage = "Image" + (i + 1);
            note.put(idImage, imagesArray.get(i));
        }
        long r = db.insert("all_notes", null, note);

        return r != -1;
    }

    @NotNull
    public ArrayList<Notes> getAllNotes(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM all_notes WHERE user_id=?", new String[]{userId});

        ArrayList<Notes> notesArray = new ArrayList<>();
        while (cursor.moveToNext()) {
            int noteId = cursor.getInt(0);
            String title = cursor.getString(2);
            String description = cursor.getString(3);
            ArrayList<String> imagesArray = new ArrayList<>();
            for (int i = 4; i < 14; i++) {
                if (cursor.getString(i) == null)
                    break;
                String image = cursor.getString(i);
                imagesArray.add(image);
            }
            Notes note = new Notes(noteId, title, description, imagesArray);
            notesArray.add(note);
        }
        cursor.close();
        return notesArray;
    }

    public void updateNote(String data, int chooser, int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newNote = new ContentValues();

        if (chooser == 1)   // chooser is passed 1 for updating title
            newNote.put("Title", data);
        else               // chooser is passed 2 for updating description
            newNote.put("Description", data);

        db.update("all_notes", newNote, "id = " + noteId, null);
    }

    public void updateImagesInNote(ArrayList<String> imagesArray, int counter, int noteId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newNote = new ContentValues();

        for (int i = counter; i < imagesArray.size(); i++) {
            String idImage = "Image" + (i + 1);
            newNote.put(idImage, imagesArray.get(i));
        }
        db.update("all_notes", newNote, "id = " + noteId, null);
    }

    public void deleteNote(int noteId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("all_notes", "id =" + noteId, null);
    }
}