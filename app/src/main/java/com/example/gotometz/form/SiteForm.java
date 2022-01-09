package com.example.gotometz.form;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gotometz.R;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.listeners.CancelFormListener;
import com.example.gotometz.listeners.SubmitFormSiteListener;
import com.example.gotometz.dbmodels.Category;

import java.util.List;
/**
 * Fragment of sites/POI form to be added in other activities
 */
public class SiteForm extends AppCompatActivity {
    private TextView id;
    private EditText label;
    private EditText latitude;
    private EditText longitude;
    private EditText postalAddress;
    private EditText summary;
    private Spinner category;
    private boolean isAdding = true;

    @SuppressLint({"SetTextI18n", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_form);

        // If we don't find values we are in editing mode
        if (this.getIntent().getExtras() != null && this.getIntent().hasExtra("id"))
            isAdding = false;

        TextView title = findViewById(R.id.addOrUpdateSiteTV);
        id = findViewById(R.id.siteIdTV);
        label = findViewById(R.id.siteLabelET);
        latitude = findViewById(R.id.siteLatitudeET);
        longitude = findViewById(R.id.siteLongitudeET);
        postalAddress = findViewById(R.id.sitePostalAddrET);
        summary = findViewById(R.id.siteSummaryET);
        category = findViewById(R.id.siteCategorySPN);
        CategoryService categoryDao = CategoryService.getInstance(this);
        List<Category> categories = categoryDao.findAll();
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        Button submit = findViewById(R.id.submitBTN);
        Button cancel = findViewById(R.id.cancelBTN);
        submit.setOnClickListener(new SubmitFormSiteListener(this));
        cancel.setOnClickListener(new CancelFormListener(this));

        if (isAdding) {
            title.setText(this.getString(R.string.addSite));
            submit.setText(R.string.add);
        } else {
            title.setText(this.getString(R.string.updateSite) + " " + this.getIntent().getExtras().getString("label"));
            id.setText(String.valueOf(this.getIntent().getExtras().getLong("id")));
            label.setText(this.getIntent().getExtras().getString("label"));
            latitude.setText(String.valueOf(this.getIntent().getExtras().getDouble("latitude")));
            longitude.setText(String.valueOf(this.getIntent().getExtras().getDouble("longitude")));
            postalAddress.setText(this.getIntent().getExtras().getString("postalAddress"));
            summary.setText(this.getIntent().getExtras().getString("summary"));
            category.setSelection(categoryAdapter.getPosition(categoryDao.findById(this.getIntent().getExtras().getLong("categoryId"))));
            submit.setText(R.string.update);
        }

        // Si on ajoute depuis la carte on remplit la position de l'utilisateur
        if (this.getIntent().getExtras() != null && this.getIntent().hasExtra("latitudeForMap")) {
            latitude.setText(String.valueOf(this.getIntent().getExtras().getDouble("latitudeForMap")));
            longitude.setText(String.valueOf(this.getIntent().getExtras().getDouble("longitudeForMap")));
        }
    }

    public TextView getId() {
        return id;
    }

    public EditText getLabel() {
        return label;
    }

    public EditText getLatitude() {
        return latitude;
    }

    public EditText getLongitude() {
        return longitude;
    }

    public EditText getPostalAddress() {
        return postalAddress;
    }

    public Spinner getCategory() {
        return category;
    }

    public EditText getSummary() {
        return summary;
    }
}
