package com.gooutinmetz.site;

import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.category.CategoryModel;
import com.gooutinmetz.db.DAOService;

import java.util.List;

public class SiteDAOService implements DAOService<SiteModel> {

    private static SiteDAOService instance;
    private final SQLiteSiteDao sqLiteSiteDao;

    private SiteDAOService(Context context){
        sqLiteSiteDao = SQLiteSiteDao.getInstance(context);
    }

    public static SiteDAOService getInstance(Context context) {
        if (instance == null)
            instance = new SiteDAOService(context);
        return instance;
    }

    @Override
    public long create(SiteModel object) {
        return sqLiteSiteDao.create(object);
    }

    @Override
    public int update(SiteModel object) {
        return sqLiteSiteDao.update(object);
    }

    @Override
    public int delete(long id) {
        return sqLiteSiteDao.delete(id);
    }

    @Override
    public SiteModel findById(long id) {
        return sqLiteSiteDao.findById(id);
    }

    @Override
    public List<SiteModel> findAll() {
        return sqLiteSiteDao.findAll();
    }

    @Override
    public Cursor getWithCursor(long id) {
        return sqLiteSiteDao.getWithCursor(id);
    }

    public List<SiteModel> findByCategory(CategoryModel category) {
        return sqLiteSiteDao.findByCategory(category);
    }

    public boolean isCategoryUsed(CategoryModel category) {
        return sqLiteSiteDao.isCategoryUsed(category);
    }
}
