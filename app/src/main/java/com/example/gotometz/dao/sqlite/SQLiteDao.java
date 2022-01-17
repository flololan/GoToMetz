package com.example.gotometz.dao.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
* Abstract class for data manipulation
* */

public abstract class SQLiteDao<T> {

    protected SQLiteDatabase sqLiteDatabase;
    protected DatabaseHelper databaseHelper;
    protected Context context;

    public SQLiteDao(Context context) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(this.context);
    }

    /*
     *   Write into DB
     */
    public void openWritable() throws SQLException {
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    /*
     *   Read from DB
     */
    public void openReadable() throws SQLException {
        sqLiteDatabase = databaseHelper.getReadableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    public abstract T cursorToObject(Cursor cursor);
}
