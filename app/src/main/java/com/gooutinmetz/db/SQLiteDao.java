package com.gooutinmetz.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLiteDao<T> {

    protected SQLiteDatabase sqLiteDatabase;
    protected DatabaseHelper databaseHelper;
    protected Context context;

    public SQLiteDao(Context context) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(this.context);
    }

    /** Write in the BDD */
    public void openWritable() throws SQLException {
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    /** Read in the BDD */
    public void openReadable() throws SQLException {
        sqLiteDatabase = databaseHelper.getReadableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    public abstract T cursorToObject(Cursor cursor);
}
