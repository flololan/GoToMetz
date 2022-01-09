package com.example.gotometz.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.gotometz.form.CategoryForm;
import com.example.gotometz.dbmodels.Category;

/**
 * Listener for opening the adding a category form
 */
public class AddOrEditCategoryFormListener implements View.OnClickListener {
    private Activity activity;
    private Category category;

    public AddOrEditCategoryFormListener(Activity activity, Category category) {
        this.activity = activity;
        this.category = category;
    }

    @Override
    public void onClick(View v) {
         Intent intent = new Intent(activity, CategoryForm.class);
        if (category != null) {
            intent.putExtra("id", category.getId());
            intent.putExtra("label", category.getLabel());
        }
        activity.startActivityForResult(intent,1);
    }
}
