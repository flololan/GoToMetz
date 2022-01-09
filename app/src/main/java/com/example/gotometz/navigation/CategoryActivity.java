package com.example.gotometz.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotometz.R;
import com.example.gotometz.listeners.AddOrEditCategoryFormListener;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.list_views.CategoryListView;
import com.example.gotometz.dbmodels.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private CategoryService categoryService;
    private List<Category> categoryList;
    private CategoryListView categoryListView;
    private ListView listView;

    public FloatingActionButton addCategoryBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        // Add Menu
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.menu);
        bottomNavigationView.setSelectedItemId(R.id.categoryMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new Menu(this));

        // Getting data from DB
        categoryService = CategoryService.getInstance(this);
        categoryList = categoryService.findAll();

        // Filling category list
        categoryListView = new CategoryListView(this, categoryList);
        listView = findViewById(R.id.categoryLV);
        listView.setAdapter(categoryListView);

        // Add category button

        FloatingActionButton addCategoryBTN = findViewById(R.id.addCategoryBTN);
        addCategoryBTN.setOnClickListener(new AddOrEditCategoryFormListener(this, null));
    }

    // Retrieve the data of sub activity for adding and modifying
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            // If we can't get the ID of the category, it means the user wants to add one. Otherwise we edit it.
            if (data.getLongExtra("id", -1) == -1) {
                Category category = new Category(data.getStringExtra("label"));
                category.setId(categoryService.create(category));

                categoryListView.add(category);
                categoryListView.notifyDataSetChanged();
            } else {
                Category category = new Category(data.getLongExtra("id", -1), data.getStringExtra("label"));

                categoryService.update(category);
                categoryList = categoryService.findAll();
                categoryListView = new CategoryListView(this, categoryList);
                listView.setAdapter(categoryListView);
            }
        }
    }
}
