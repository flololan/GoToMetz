package com.example.gotometz.form;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.example.gotometz.model.Category;

import java.util.ArrayList;
import java.util.List;

public class SubmitFormSiteListener implements View.OnClickListener {
    private SiteForm siteForm;

    public SubmitFormSiteListener(SiteForm siteForm) {
        this.siteForm = siteForm;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("id", Long.parseLong(siteForm.getId().getText().toString()));
        intent.putExtra("label", siteForm.getLabel().getText().toString());
        intent.putExtra("latitude", Double.parseDouble(siteForm.getLatitude().getText().toString()));
        intent.putExtra("longitude", Double.parseDouble(siteForm.getLongitude().getText().toString()));
        intent.putExtra("postalAddress", siteForm.getPostalAddress().getText().toString());
        intent.putExtra("categoryId", ((Category) siteForm.getCategory().getSelectedItem()).getId());
        intent.putExtra("summary", siteForm.getSummary().getText().toString());
        siteForm.setResult(1, intent);
        siteForm.finish();
    }
}
