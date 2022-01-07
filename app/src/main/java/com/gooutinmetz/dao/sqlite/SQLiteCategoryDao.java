package com.gooutinmetz.dao.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.dao.DAOService;
import com.gooutinmetz.category.CategoryModel;

import java.util.LinkedList;
import java.util.List;

public class SQLiteCategoryDao extends SQLiteDao<CategoryModel> implements DAOService<CategoryModel> {

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
    public long create(CategoryModel category) {
        openWritable();

        ContentValues values = putContentValues(category);

        long lastInsertedId = sqLiteDatabase.insert(DatabaseHelper.TABLE_CATEGORY,
                null,
                values);

        close();

        return lastInsertedId;
    }

    @Override
    public int update(CategoryModel category) {
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
    public CategoryModel findById(long id) {
        openReadable();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_CATEGORY,
                allColumns,
                DatabaseHelper.COLUMN_ID_CAT + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        cursor.moveToFirst();

        CategoryModel category = cursorToObject(cursor);

        cursor.close();

        close();

        return category;
    }

    @Override
    public List<CategoryModel> findAll() {
        openReadable();

        List<CategoryModel> categories = new LinkedList<>();

        String query = "SELECT  * FROM " + DatabaseHelper.TABLE_CATEGORY;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        CategoryModel category;
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
    public CategoryModel cursorToObject(Cursor cursor) {
        return new CategoryModel(cursor.getLong(0),
                cursor.getString(1));
    }

    private ContentValues putContentValues(CategoryModel category) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_LABEL, category.getLabel());

        return values;
    }
}
