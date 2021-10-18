package com.five.sixse.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.five.sixse.activity.MainActivity;
import com.five.sixse.model.Book;
import com.five.sixse.model.Note;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class NoteDBHelper extends SQLiteOpenHelper {

    //database version
    private static int db_version = 1;

    //database name
    private static String db_name = "note.db";

    //database path string
    private String db_path;


    private SQLiteDatabase db;
    private final Context context;

    //table name
    public static String TABLE_NAME = "note";

    //column names
    public static String V_NOTE = "v_note";
    public static String V_TITLE = "v_title";
    public static String V_DES = "v_des";
    public static String V_WORDS = "v_words";
    public static String V_ID = "v_id";


    public ArrayList<Book> bookList;

    public NoteDBHelper(Context context) {
        super(context, db_name, null, db_version);
        this.context = context;

        File database = context.getDatabasePath(db_name);
        if (!database.exists()) {
            // Database does not exist so copy it from assets here
            db_path = context.getDatabasePath(db_name).toString().replace(db_name, "");

        } else {
            //if database  exist get database from path
            db_path = context.getDatabasePath(db_name).toString();

        }

    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //delete database
    public void db_delete() {
        File file = new File(db_path + db_name);
        if (file.exists()) {
            file.delete();

        }
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.v("DB Exists", "db exists");

        }

        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getWritableDatabase();
            try {
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = db_path + db_name;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException {
        String outFileName = db_path + db_name;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = context.getAssets().open(db_name);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    /*
     * insert note and chapter in note table
     */
    public void insertIntoDB(String note, int vId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_NAME + " (" + V_NOTE + "," + V_ID + ") VALUES('" + note + "', '" + vId + "');";
        db.execSQL(query);

    }

    /*
    * delete note
    */
    public void delete_note(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String delete_query = " DELETE FROM " + TABLE_NAME + " WHERE " + V_ID + "='" + name + "'";
        db.execSQL(delete_query);

    }



    //get All Note from table
    public ArrayList<Note> getAllNote(String table) {
        ArrayList<Note> noteList = new ArrayList<Note>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY v_id ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNote(c.getString(c.getColumnIndex("v_note")));
                note.setV_id(c.getString(c.getColumnIndex("v_id")));
                bookList = MainActivity.dba.get_Bookmarked(c.getInt(c.getColumnIndex("v_id")), table);
                note.setV_title(bookList.get(0).getTitle());
                note.setCatId(bookList.get(0).getCatId());
                // adding to categories list
                noteList.add(note);

            } while (c.moveToNext());
        }
        return noteList;
    }

    /*
    * get note by v_id
    */
    public String GetNoteByV_Id(int vId) {

        String note = "";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE v_id=" + vId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                note = c.getString(c.getColumnIndex("v_note"));

            } while (c.moveToNext());
        }
        return note;
    }

    /*
    * id note id already exists in table  then Update Note
    */
    public void UpdateNote(int id, String val) {
        db = this.getReadableDatabase();
        ContentValues value = new ContentValues();
        value.put("v_note", "" + val);
        db.update(TABLE_NAME, value, " v_id=" + id, null);

    }
}
