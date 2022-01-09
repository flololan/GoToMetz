package com.example.gotometz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotometz.administration.DisplayCategoryFormListener;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.administration.CategoryListView;
import com.example.gotometz.model.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private CategoryService categoryService;
    private List<Category> categoryList;
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

        this.renderActivities();

        Button addCategoryBTN = findViewById(R.id.addCategoryBTN);
        addCategoryBTN.setOnClickListener(new DisplayCategoryFormListener(this, null));
    }

    protected void renderActivities() {
        categoryService = CategoryService.getInstance(this);
        categoryList = categoryService.findAll();

        categoryListView = new CategoryListView(this, categoryList);
        listView = findViewById(R.id.categoryLV);
        listView.setAdapter(categoryListView);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            Boolean createCategory = data.getLongExtra("id", -1) == -1;
            if (createCategory) {
                Category category = new Category(data.getStringExtra("label"));
                category.setId(categoryService.create(category));

                categoryListView.add(category);
                categoryListView.notifyDataSetChanged();
            } else {
                // update category
                Category category = new Category(data.getLongExtra("id", -1), data.getStringExtra("label"));

                categoryService.update(category);
                categoryList = categoryService.findAll();
                categoryListView = new CategoryListView(this, categoryList);
                listView.setAdapter(categoryListView);
            }
        }
    }
}
