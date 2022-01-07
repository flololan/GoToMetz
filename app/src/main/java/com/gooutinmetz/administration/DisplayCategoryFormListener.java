package com.gooutinmetz.administration;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gooutinmetz.category.update.CategoryForm;
import com.gooutinmetz.category.CategoryModel;

public class DisplayCategoryFormListener implements View.OnClickListener {
    private Activity activity;
    private CategoryModel category;

    public DisplayCategoryFormListener(Activity activity, CategoryModel category) {
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
