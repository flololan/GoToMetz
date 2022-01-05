package com.example.gotometz.map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gotometz.MapsActivity;
import com.example.gotometz.R;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.model.Category;

import java.util.List;

public class SearchSiteDialog extends AppCompatDialogFragment {

    private MapsActivity mapsActivity;
    private Location location;

    private SearchModalDialogListener listener;

    public SearchSiteDialog(MapsActivity mapsActivity, Location location) {
        this.mapsActivity = mapsActivity;
        this.location = location;
    }

    public interface SearchModalDialogListener {
        void onDialogPositiveClick(Location location, Category category, int radius);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SearchModalDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement SearchModalDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mapsActivity);
        LayoutInflater inflater = mapsActivity.getLayoutInflater();

        View view = inflater.inflate(R.layout.search_site, null);

        EditText siteLatitudeET = view.findViewById(R.id.siteLatitudeET);
        EditText siteLongitudeET = view.findViewById(R.id.siteLongitudeET);

        siteLatitudeET.setText(String.valueOf(location.getLatitude()));
        siteLongitudeET.setText(String.valueOf(location.getLongitude()));

        final Spinner categorySPN = view.findViewById(R.id.siteCategorySPN);
        CategoryService categoryDao = CategoryService.getInstance(mapsActivity);
        List<Category> categories = categoryDao.findAll();
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(mapsActivity, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySPN.setAdapter(categoryAdapter);

        final EditText radiusET = view.findViewById(R.id.radiusET);

        builder.setView(view);

        builder.setTitle(R.string.search);

        builder.setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogPositiveClick(location, (Category) categorySPN.getSelectedItem(), Integer.parseInt(radiusET.getText().toString()));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });

        return builder.create();
    }
}
