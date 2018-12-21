package com.example.extarc.androidpushnotification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoNotification";
    public static final String TABLE_NAME = "ToDotasks";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DETAIL = "description";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";
    public static final int VERSION = 1;

    public ReminderDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createDataBase = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_DETAIL + " TEXT, "
                + COL_DATE + " TEXT, "
                + COL_TIME + " TEXT" + ")";
        sqLiteDatabase.execSQL(createDataBase);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        String droptableSQL = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(droptableSQL);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    boolean addToDolist(String title, String description, String date, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DETAIL, description);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    boolean updateToDolist(int id, String name, String dept, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, name);
        contentValues.put(COL_DETAIL, dept);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);
        return db.update(TABLE_NAME, contentValues, COL_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    boolean deleteToDolist(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    Cursor getAllToDolists() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}