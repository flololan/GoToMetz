package com.gooutinmetz.category;

import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.db.DAOService;

import java.util.List;

public class CategoryDAOService implements DAOService<CategoryModel> {

    private static CategoryDAOService instance;
    private SQLiteDaoCategory sqLiteDaoCategory;

    private CategoryDAOService(Context context){
        sqLiteDaoCategory = SQLiteDaoCategory.getInstance(context);
    }

    public static CategoryDAOService getInstance(Context context) {
        if (instance == null)
            instance = new CategoryDAOService(context);
        return instance;
    }

    @Override
    public long create(CategoryModel object) {
        return sqLiteDaoCategory.create(object);
    }

    @Override
    public int update(CategoryModel object) {
        return sqLiteDaoCategory.update(object);
    }

    @Override
    public int delete(long id) {
        return sqLiteDaoCategory.delete(id);
    }

    @Override
    public CategoryModel findById(long id) {
        return sqLiteDaoCategory.findById(id);
    }

    @Override
    public List<CategoryModel> findAll() {
        return sqLiteDaoCategory.findAll();
    }

    @Override
    public Cursor getWithCursor(long id) {
        return sqLiteDaoCategory.getWithCursor(id);
    }
}
