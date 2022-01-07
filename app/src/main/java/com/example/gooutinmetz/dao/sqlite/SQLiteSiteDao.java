package com.example.gooutinmetz.dao.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.gooutinmetz.dao.ServiceDAO;
import com.example.gooutinmetz.model.Category;
import com.example.gooutinmetz.model.Site;

import java.util.LinkedList;
import java.util.List;

public class SQLiteSiteDao extends SQLiteDao<Site> implements ServiceDAO<Site> {

    @SuppressLint("StaticFieldLeak")
    private static SQLiteSiteDao instance;

    private static final String[] allColumns = { DatabaseHelper.COLUMN_ID_SITE, DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_LATITUDE, DatabaseHelper.COLUMN_LONGITUDE, DatabaseHelper.COLUMN_POSTALADDR,
            DatabaseHelper.COLUMN_CATEGORY_ID, DatabaseHelper.COLUMN_SUMMARY };

    public SQLiteSiteDao(Context context) {
        super(context);
    }

    public static SQLiteSiteDao getInstance(Context context) {
        if (instance == null)
            instance = new SQLiteSiteDao(context);

        return instance;
    }

    @Override
    public long create(Site site) {
        openWritable();

        ContentValues values = putContentValues(site);

        long lastInsertedId = sqLiteDatabase.insert(DatabaseHelper.TABLE_SITE,
                null,
                values);

        close();

        return lastInsertedId;
    }

    @Override
    public int update(Site site) {
        openWritable();

        ContentValues values = putContentValues(site);

        return sqLiteDatabase.update(DatabaseHelper.TABLE_SITE, values, DatabaseHelper.COLUMN_ID_SITE + " = ?", new String[] { String.valueOf(site.getId()) });
    }

    @Override
    public int delete(long id) {
        openWritable();

        int returnedId = sqLiteDatabase.delete(DatabaseHelper.TABLE_SITE,
                DatabaseHelper.COLUMN_ID_SITE + " = ?",
                new String[] { String.valueOf(id) });

        close();

        return returnedId;
    }

    @Override
    public Site findById(long id) {
        openReadable();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_SITE,
                allColumns,
                DatabaseHelper.COLUMN_ID_SITE + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        cursor.moveToFirst();

        Site site = cursorToObject(cursor);

        cursor.close();

        close();

        return site;
    }

    @Override
    public List<Site> findAll() {
        openReadable();

        List<Site> sites = new LinkedList<>();

        String query = "SELECT  * FROM " + DatabaseHelper.TABLE_SITE;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Site site;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            site = cursorToObject(cursor);
            sites.add(site);

            cursor.moveToNext();
        }
        cursor.close();

        close();

        return sites;
    }

    public List<Site> findByCategory(Category category) {
        openReadable();

        List<Site> sites = new LinkedList<>();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_SITE,
                allColumns,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(category.getId()) },
                null, null, null, null);

        Site site;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            site = cursorToObject(cursor);
            sites.add(site);

            cursor.moveToNext();
        }
        cursor.close();

        close();

        return sites;
    }

    @Override
    public Cursor getWithCursor(long id) {
        return sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_SITE + " WHERE id = ?",
                new String[] { String.valueOf(id) });
    }

    @Override
    public Site cursorToObject(Cursor cursor) {
        return new Site(cursor.getLong(0),
                cursor.getString(1), cursor.getDouble(2),
                cursor.getDouble(3), cursor.getString(4),
                new Category(cursor.getLong(5), SQLiteCategoryDao.getInstance(this.context).findById(cursor.getLong(5)).getLabel()),
                cursor.getString(6));
    }

    public boolean isCategoryUsed(Category category) {
        openReadable();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_SITE,
                allColumns,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(category.getId()) },
                null, null, null, null);

        boolean result = cursor.moveToFirst();

        cursor.close();

        close();

        return result;
    }

    private ContentValues putContentValues(Site site) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_NAME, site.getLabel());
        values.put(DatabaseHelper.COLUMN_LATITUDE, site.getLatitude());
        values.put(DatabaseHelper.COLUMN_LONGITUDE, site.getLongitude());
        values.put(DatabaseHelper.COLUMN_POSTALADDR, site.getPostalAddress());
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, site.getCategory().getId());
        values.put(DatabaseHelper.COLUMN_SUMMARY, site.getSummary());

        return values;
    }
}
