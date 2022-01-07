package com.gooutinmetz.site;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.db.DatabaseHelper;
import com.gooutinmetz.category.CategoryDaoSQLite;
import com.gooutinmetz.db.SQLiteDao;
import com.gooutinmetz.db.DAOService;
import com.gooutinmetz.category.CategoryModel;

import java.util.LinkedList;
import java.util.List;

public class SiteDaoSQLite extends SQLiteDao<SiteModel> implements DAOService<SiteModel> {

    @SuppressLint("StaticFieldLeak")
    private static SiteDaoSQLite instance;

    private static final String[] allColumns = { DatabaseHelper.COLUMN_ID_SITE, DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_LATITUDE, DatabaseHelper.COLUMN_LONGITUDE, DatabaseHelper.COLUMN_POSTALADDR,
            DatabaseHelper.COLUMN_CATEGORY_ID, DatabaseHelper.COLUMN_SUMMARY };

    public SiteDaoSQLite(Context context) {
        super(context);
    }

    public static SiteDaoSQLite getInstance(Context context) {
        if (instance == null)
            instance = new SiteDaoSQLite(context);

        return instance;
    }

    @Override
    public long create(SiteModel site) {
        openWritable();

        ContentValues values = putContentValues(site);

        long lastInsertedId = sqLiteDatabase.insert(DatabaseHelper.TABLE_SITE,
                null,
                values);

        close();

        return lastInsertedId;
    }

    @Override
    public int update(SiteModel site) {
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
    public SiteModel findById(long id) {
        openReadable();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_SITE,
                allColumns,
                DatabaseHelper.COLUMN_ID_SITE + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        cursor.moveToFirst();

        SiteModel site = cursorToObject(cursor);

        cursor.close();

        close();

        return site;
    }

    @Override
    public List<SiteModel> findAll() {
        openReadable();

        List<SiteModel> sites = new LinkedList<>();

        String query = "SELECT  * FROM " + DatabaseHelper.TABLE_SITE;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        SiteModel site;
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

    public List<SiteModel> findByCategory(CategoryModel category) {
        openReadable();

        List<SiteModel> sites = new LinkedList<>();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_SITE,
                allColumns,
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(category.getId()) },
                null, null, null, null);

        SiteModel site;
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
    public SiteModel cursorToObject(Cursor cursor) {
        return new SiteModel(cursor.getLong(0),
                cursor.getString(1), cursor.getDouble(2),
                cursor.getDouble(3), cursor.getString(4),
                new CategoryModel(cursor.getLong(5), CategoryDaoSQLite.getInstance(this.context).findById(cursor.getLong(5)).getLabel()),
                cursor.getString(6));
    }

    public boolean isCategoryUsed(CategoryModel category) {
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

    private ContentValues putContentValues(SiteModel site) {
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
