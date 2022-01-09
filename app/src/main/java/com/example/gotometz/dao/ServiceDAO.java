package com.example.gotometz.dao;

import android.database.Cursor;

import java.util.List;

/**
 * Interface for defining manipulation methods
 * @param <T>
 */
public interface ServiceDAO<T> {

    long create(T object);

    int update(T object);

    int delete(long id);

    T findById(long id);

    List<T> findAll();

    Cursor getWithCursor(long id);
}
