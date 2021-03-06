package com.example.gotometz.form;

import android.content.Intent;
import android.view.View;

public class SubmitFormCategoryListener implements View.OnClickListener {
    private CategoryForm categoryForm;

    public SubmitFormCategoryListener(CategoryForm categoryForm) {
        this.categoryForm = categoryForm;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("id", Long.parseLong(categoryForm.getId().getText().toString()));
        intent.putExtra("label", categoryForm.getLabel().getText().toString());
        categoryForm.setResult(1, intent);
        categoryForm.finish();
    }
}
