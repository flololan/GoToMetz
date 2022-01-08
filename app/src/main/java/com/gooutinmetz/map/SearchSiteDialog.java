package com.gooutinmetz.map;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gooutinmetz.R;
import com.gooutinmetz.category.CategoryDaoService;
import com.gooutinmetz.category.CategoryModel;

import java.util.List;

public class SearchSiteDialog extends AppCompatDialogFragment {

    private final MapActivity mapActivity;
    private final Location location;

    private SearchModalDialogListener listener;

    public SearchSiteDialog(MapActivity mapActivity, Location location) {
        this.mapActivity = mapActivity;
        this.location = location;
    }

    public interface SearchModalDialogListener {
        void onDialogPositiveClick(Location location, CategoryModel category, int radius);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SearchModalDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement SearchModalDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mapActivity);
        LayoutInflater inflater = mapActivity.getLayoutInflater();

        View view = inflater.inflate(R.layout.search_site, null);

        EditText siteLatitudeET = view.findViewById(R.id.siteLatitudeET);
        EditText siteLongitudeET = view.findViewById(R.id.siteLongitudeET);

        siteLatitudeET.setText(String.valueOf(location.getLatitude()));
        siteLongitudeET.setText(String.valueOf(location.getLongitude()));

        final Spinner categorySPN = view.findViewById(R.id.siteCategorySPN);
        CategoryDaoService categoryDao = CategoryDaoService.getInstance(mapActivity);
        List<CategoryModel> categories = categoryDao.findAll();
        ArrayAdapter<CategoryModel> categoryAdapter = new ArrayAdapter<>(mapActivity, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySPN.setAdapter(categoryAdapter);

        final EditText radiusET = view.findViewById(R.id.radiusET);

        builder.setView(view);

        builder.setTitle(R.string.search);

        builder.setPositiveButton(R.string.search, (dialog, id) -> listener.onDialogPositiveClick(location, (CategoryModel) categorySPN.getSelectedItem(), Integer.parseInt(radiusET.getText().toString())));

        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {});

        return builder.create();
    }
}
