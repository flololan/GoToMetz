package com.gooutinmetz.dao;

import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.dao.sqlite.SQLiteCategoryDao;
import com.gooutinmetz.model.Category;

import java.util.List;

public class CategoryService implements ServiceDAO<Category> {

    private static CategoryService instance;
    private SQLiteCategoryDao sqLiteCategoryDao;

    private CategoryService(Context context){
        sqLiteCategoryDao = SQLiteCategoryDao.getInstance(context);
    }

    public static CategoryService getInstance(Context context) {
        if (instance == null)
            instance = new CategoryService(context);
        return instance;
    }

    @Override
    public long create(Category object) {
        return sqLiteCategoryDao.create(object);
    }

    @Override
    public int update(Category object) {
        return sqLiteCategoryDao.update(object);
    }

    @Override
    public int delete(long id) {
        return sqLiteCategoryDao.delete(id);
    }

    @Override
    public Category findById(long id) {
        return sqLiteCategoryDao.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return sqLiteCategoryDao.findAll();
    }

    @Override
    public Cursor getWithCursor(long id) {
        return sqLiteCategoryDao.getWithCursor(id);
    }
}
