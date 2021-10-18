package com.five.sixse.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.five.sixse.model.Book;
import com.five.sixse.model.Category;

public class MainDBHelper extends SQLiteOpenHelper {

    private static int db_version = 1;
    private static String db_name = "lorem_lpsum.db";
    private String db_path;
    private SQLiteDatabase db;
    private final Context con;

    public MainDBHelper(Context con) {
        super(con, db_name, null, db_version);
        // TODO Auto-generated constructor stub
        this.con = con;
        db_path = con.getDatabasePath(db_name).toString().replace(db_name, "");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    public void createDB() throws IOException {

        if (checkDB()) {
        } else if (!checkDB()) {
            this.getReadableDatabase();
            copyDB();
        }

    }

    private boolean checkDB() {

        SQLiteDatabase cDB = null;
        try {
            cDB = SQLiteDatabase.openDatabase(db_path + db_name, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (cDB != null) {
            cDB.close();
        }
        return cDB != null ? true : false;
    }


    private void copyDB() throws IOException {
        InputStream inputFile = con.getAssets().open(db_name);
        String outFileName = db_path + db_name;
        OutputStream outFile = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputFile.read(buffer)) > 0) {
            outFile.write(buffer, 0, length);
        }
        outFile.flush();
        outFile.close();
        inputFile.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + Constants.VAR_HINDI);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.VAR_ENG);

    }

    //when user change language from reading area .whe should get right content from table with language
    //get description
    public String getDescription(int id, String tbl_name) {
        String des = "";
        String selectQuery = "SELECT  * FROM " + tbl_name + " where Id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                des = c.getString(c.getColumnIndex("Description"));

            } while (c.moveToNext());
        }
        return des;
    }

    //when user change language from reading area .whe should get right content from table with language
    //get chapter title
    public String getTitle(int id, String tbl_name) {
        String title = "";
        String selectQuery = "SELECT  * FROM " + tbl_name + " where Id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                title = c.getString(c.getColumnIndex("Title"));
            } while (c.moveToNext());
        }
        return title;
    }

    //set actionbar title with selected language
    public String getActionBarTitle(int id, String tbl_name) {
        String title = "";
        String selectQuery = "SELECT  * FROM " + tbl_name + " where Id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                title = c.getString(c.getColumnIndex("Title"));
            } while (c.moveToNext());
        }
        return title;
    }

    /*
    *get All chapter of book from table with out filter
    */
    public ArrayList<Book> getBookChapterWithCategory(int categoryId, String tbl_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + tbl_name + "  WHERE Category_Id = " + categoryId;
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        //}
        return bookArrayList;
    }

    /*
  *get All chapter of table with out filter
  */
    public ArrayList<Category> getMainCategory(String tbl_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM  " + tbl_name, null);
        if (cur.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategoryId(cur.getInt(cur.getColumnIndex("Id")));
                category.setTitle(cur.getString(cur.getColumnIndex("Title")));
                categoryArrayList.add(category);

            } while (cur.moveToNext());
        }
        //}
        return categoryArrayList;
    }

    public ArrayList<Book> getAllBookChapterFromTable(String tbl_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + tbl_name + "  WHERE 1 ";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }


    //get only chapter from table which are marked
    public ArrayList<Book> get_Bookmarked(int id, String tbl_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String[] args = new String[]{};
        Cursor cur = db.rawQuery("SELECT * FROM  " + tbl_name + "  WHERE Id=" + id, null);

        if (cur.moveToFirst()) {
            do {

                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);

            } while (cur.moveToNext());
        }

        return bookArrayList;
    }


}
