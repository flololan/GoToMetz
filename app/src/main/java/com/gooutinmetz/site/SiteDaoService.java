package com.gooutinmetz.site;

import android.content.Context;
import android.database.Cursor;

import com.gooutinmetz.category.CategoryModel;
import com.gooutinmetz.db.DAOService;

import java.util.List;

public class SiteDaoService implements DAOService<SiteModel> {

    private static SiteDaoService instance;
    private final SiteDaoSQLite siteDaoSQLite;

    private SiteDaoService(Context context){
        siteDaoSQLite = SiteDaoSQLite.getInstance(context);
    }

    public static SiteDaoService getInstance(Context context) {
        if (instance == null)
            instance = new SiteDaoService(context);
        return instance;
    }

    @Override
    public long create(SiteModel object) {
        return siteDaoSQLite.create(object);
    }

    @Override
    public int update(SiteModel object) {
        return siteDaoSQLite.update(object);
    }

    @Override
    public int delete(long id) {
        return siteDaoSQLite.delete(id);
    }

    @Override
    public SiteModel findById(long id) {
        return siteDaoSQLite.findById(id);
    }

    @Override
    public List<SiteModel> findAll() {
        return siteDaoSQLite.findAll();
    }

    @Override
    public Cursor getWithCursor(long id) {
        return siteDaoSQLite.getWithCursor(id);
    }

    public List<SiteModel> findByCategory(CategoryModel category) {
        return siteDaoSQLite.findByCategory(category);
    }

    public boolean isCategoryUsed(CategoryModel category) {
        return siteDaoSQLite.isCategoryUsed(category);
    }
}
