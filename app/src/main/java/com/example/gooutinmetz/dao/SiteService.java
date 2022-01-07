package com.example.gooutinmetz.dao;

import android.content.Context;
import android.database.Cursor;

import com.example.gooutinmetz.dao.sqlite.SQLiteSiteDao;
import com.example.gooutinmetz.model.Category;
import com.example.gooutinmetz.model.Site;

import java.util.List;

public class SiteService implements ServiceDAO<Site>{

    private static SiteService instance;
    private SQLiteSiteDao sqLiteSiteDao;

    private SiteService(Context context){
        sqLiteSiteDao = SQLiteSiteDao.getInstance(context);
    }

    public static SiteService getInstance(Context context) {
        if (instance == null)
            instance = new SiteService(context);
        return instance;
    }

    @Override
    public long create(Site object) {
        return sqLiteSiteDao.create(object);
    }

    @Override
    public int update(Site object) {
        return sqLiteSiteDao.update(object);
    }

    @Override
    public int delete(long id) {
        return sqLiteSiteDao.delete(id);
    }

    @Override
    public Site findById(long id) {
        return sqLiteSiteDao.findById(id);
    }

    @Override
    public List<Site> findAll() {
        return sqLiteSiteDao.findAll();
    }

    @Override
    public Cursor getWithCursor(long id) {
        return sqLiteSiteDao.getWithCursor(id);
    }

    public List<Site> findByCategory(Category category) {
        return sqLiteSiteDao.findByCategory(category);
    }

    public boolean isCategoryUsed(Category category) {
        return sqLiteSiteDao.isCategoryUsed(category);
    }
}
