package com.example.gotometz.dao.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.gotometz.dao.ServiceDAO;
import com.example.gotometz.model.Category;

import java.util.LinkedList;
import java.util.List;

public class SQLiteCategoryDao extends SQLiteDao<Category> implements ServiceDAO<Category> {

    @SuppressLint("StaticFieldLeak")
    private static SQLiteCategoryDao instance;

    private static final String[] allColumns = { DatabaseHelper.COLUMN_ID_CAT, DatabaseHelper.COLUMN_LABEL };

    public SQLiteCategoryDao(Context context) {
        super(context);
    }

    public static SQLiteCategoryDao getInstance(Context context) {
        if (instance == null)
            instance = new SQLiteCategoryDao(context);

        return instance;
    }

    @Override
    public long create(Category category) {
        openWritable();

        ContentValues values = putContentValues(category);

        long lastInsertedId = sqLiteDatabase.insert(DatabaseHelper.TABLE_CATEGORY,
                null,
                values);

        close();

        return lastInsertedId;
    }

    @Override
    public int update(Category category) {
        openWritable();

        ContentValues values = putContentValues(category);

        int returnedId = sqLiteDatabase.update(DatabaseHelper.TABLE_CATEGORY, values, DatabaseHelper.COLUMN_ID_CAT + " = ?", new String[] { String.valueOf(category.getId()) });

        return returnedId;
    }

    @Override
    public int delete(long id) {
        openWritable();

        int returnedId = sqLiteDatabase.delete(DatabaseHelper.TABLE_CATEGORY,
                DatabaseHelper.COLUMN_ID_CAT + " = ?",
                new String[] { String.valueOf(id) });

        close();

        return returnedId;
    }

    @Override
    @Nullable
    public Category findById(long id) {
        openReadable();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_CATEGORY,
                allColumns,
                DatabaseHelper.COLUMN_ID_CAT + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        if (cursor.moveToFirst()) {
            Category category = cursorToObject(cursor);

            cursor.close();
            close();
            return category;
        }

        cursor.close();
        close();

        return null;
    }

    @Override
    public List<Category> findAll() {
        openReadable();

        List<Category> categories = new LinkedList<>();

        String query = "SELECT  * FROM " + DatabaseHelper.TABLE_CATEGORY;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Category category;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            category = cursorToObject(cursor);
            categories.add(category);

            cursor.moveToNext();
        }
        cursor.close();

        close();

        return categories;
    }

    @Override
    public Cursor getWithCursor(long id) {
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CATEGORY + " WHERE id = ?",
                new String[] { String.valueOf(id) });
    }

    @Override
    public Category cursorToObject(Cursor cursor) {
        return new Category(cursor.getLong(0),
                cursor.getString(1));
    }

    private ContentValues putContentValues(Category category) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_LABEL, category.getLabel());

        return values;
    }
}
