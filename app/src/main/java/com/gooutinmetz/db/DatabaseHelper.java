package com.gooutinmetz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
    Creation bdd
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance = null;

    public static final String TABLE_CATEGORY = "category";
    public static final String COLUMN_ID_CAT = "id";
    public static final String COLUMN_LABEL = "label";

    public static final String TABLE_SITE = "site";
    public static final String COLUMN_ID_SITE = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_POSTALADDR = "postalAddr";
    public static final String COLUMN_CATEGORY_ID = "id_category";
    public static final String COLUMN_SUMMARY = "summary";

    private static final String DATABASE_NAME = "sortirametz.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY
            + "(" + COLUMN_ID_CAT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LABEL + " TEXT NOT NULL);";

    private static final String DATABASE_CREATE_SITE = "CREATE TABLE "
            + TABLE_SITE
            + "(" + COLUMN_ID_SITE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_LATITUDE + " DOUBLE NOT NULL, "
            + COLUMN_LONGITUDE + " DOUBLE NOT NULL, "
            + COLUMN_POSTALADDR + " TEXT NOT NULL, "
            + COLUMN_CATEGORY_ID + " LONG NOT NULL, "
            + COLUMN_SUMMARY + " TEXT NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_ID_CAT + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_CATEGORY);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Update DB, from version " + oldVersion + " to version "
                        + newVersion + ". It erases previous data.");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        super.onDowngrade(sqLiteDatabase, oldVersion, newVersion);
    }
}
