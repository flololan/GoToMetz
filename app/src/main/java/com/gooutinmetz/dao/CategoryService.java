package com.gooutinmetz.dao;

import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.dao.sqlite.SQLiteCategoryDao;
import com.gooutinmetz.category.CategoryModel;

import java.util.List;

public class CategoryService implements ServiceDAO<CategoryModel> {

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
    public long create(CategoryModel object) {
        return sqLiteCategoryDao.create(object);
    }

    @Override
    public int update(CategoryModel object) {
        return sqLiteCategoryDao.update(object);
    }

    @Override
    public int delete(long id) {
        return sqLiteCategoryDao.delete(id);
    }

    @Override
    public CategoryModel findById(long id) {
        return sqLiteCategoryDao.findById(id);
    }

    @Override
    public List<CategoryModel> findAll() {
        return sqLiteCategoryDao.findAll();
    }

    @Override
    public Cursor getWithCursor(long id) {
        return sqLiteCategoryDao.getWithCursor(id);
    }
}
