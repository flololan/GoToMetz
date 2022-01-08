package com.gooutinmetz.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface DAOService<T> {

    long create(T object);

    int update(T object);

    int delete(long id);

    T findById(long id);

    List<T> findAll();

    Cursor getWithCursor(long id);
}
