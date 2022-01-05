package com.example.gotometz.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Category {

    private long id;
    private String label;

    public Category() { }

    public Category(long id, String label) {
        this.setId(id);
        this.setLabel(label);
    }

    public Category(String label) {
        this.setLabel(label);
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @NonNull
    @Override
    public String toString() {
        return label;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                Objects.equals(label, category.label);
    }

    @SuppressLint("NewApi")
    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }


    public static Category fromContentValues(ContentValues values) {
        final Category category = new Category();

        if (values.containsKey("id")) category.setId(values.getAsLong("id"));
        if (values.containsKey("label")) category.setLabel(values.getAsString("label"));

        return category;
    }
}
