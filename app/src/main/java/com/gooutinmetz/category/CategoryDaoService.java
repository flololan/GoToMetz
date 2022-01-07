package com.gooutinmetz.category;

import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.db.DAOService;

import java.util.List;

public class CategoryDaoService implements DAOService<CategoryModel> {

    private static CategoryDaoService instance;
    private CategoryDaoSQLite categoryDaoSQLite;

    private CategoryDaoService(Context context){
        categoryDaoSQLite = CategoryDaoSQLite.getInstance(context);
    }

    public static CategoryDaoService getInstance(Context context) {
        if (instance == null)
            instance = new CategoryDaoService(context);
        return instance;
    }

    @Override
    public long create(CategoryModel object) {
        return categoryDaoSQLite.create(object);
    }

    @Override
    public int update(CategoryModel object) {
        return categoryDaoSQLite.update(object);
    }

    @Override
    public int delete(long id) {
        return categoryDaoSQLite.delete(id);
    }

    @Override
    public CategoryModel findById(long id) {
        return categoryDaoSQLite.findById(id);
    }

    @Override
    public List<CategoryModel> findAll() {
        return categoryDaoSQLite.findAll();
    }

    @Override
    public Cursor getWithCursor(long id) {
        return categoryDaoSQLite.getWithCursor(id);
    }
}
