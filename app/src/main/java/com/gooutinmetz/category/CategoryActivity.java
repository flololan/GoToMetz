package com.gooutinmetz.category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gooutinmetz.menu.Menu;
import com.gooutinmetz.category.update.DisplayCategoryFormListener;
import com.gooutinmetz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private CategoryDaoService categoryDAOService;
    private List<CategoryModel> categoryList;
    private CategoryListView categoryListView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        // Menu
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.menu);
        bottomNavigationView.setSelectedItemId(R.id.categoryMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new Menu(this));

        // Retrieve the data from db
        categoryDAOService = CategoryDaoService.getInstance(this);
        categoryList = categoryDAOService.findAll();

        // Fill the category list
        categoryListView = new CategoryListView(this, categoryList);
        listView = findViewById(R.id.categoryLV);
        listView.setAdapter(categoryListView);

        // Button to add a category
        Button addCategoryBTN = findViewById(R.id.addCategoryBTN);
        addCategoryBTN.setOnClickListener(new DisplayCategoryFormListener(this, null));
    }


    /**
     * We retrieve the files
     * @param requestCode code started with
     * @param resultCode code returned
     * @param data extra data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            // If we don't retrieve the id of the categorie, then we want to add one.
            if (data.getLongExtra("id", -1) == -1) {
                CategoryModel category = new CategoryModel(data.getStringExtra("label"));
                category.setId(categoryDAOService.create(category));

                categoryListView.add(category);
                categoryListView.notifyDataSetChanged();
            // else we want to modify it
            } else {
                CategoryModel category = new CategoryModel(data.getLongExtra("id", -1), data.getStringExtra("label"));

                categoryDAOService.update(category);
                categoryList = categoryDAOService.findAll();
                categoryListView = new CategoryListView(this, categoryList);
                listView.setAdapter(categoryListView);
            }
        }
    }
}
