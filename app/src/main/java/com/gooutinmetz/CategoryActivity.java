package com.gooutinmetz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gooutinmetz.administration.CategoryListView;
import com.gooutinmetz.administration.DisplayCategoryFormListener;
import com.gooutinmetz.dao.CategoryService;
import com.gooutinmetz.model.Category;
import com.example.gooutinmetz.R;
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

        // Récupération des données de la base
        categoryService = CategoryService.getInstance(this);
        categoryList = categoryService.findAll();

        // Remplit la liste des catégories
        categoryListView = new CategoryListView(this, categoryList);
        listView = findViewById(R.id.categoryLV);
        listView.setAdapter(categoryListView);

        // Bouton pour ajouter une catégorie
        Button addCategoryBTN = findViewById(R.id.addCategoryBTN);
        addCategoryBTN.setOnClickListener(new DisplayCategoryFormListener(this, null));
    }

    // On récupère les données venant des modales d'ajout et de modification de catégories
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            // Si on ne récupère pas l'id de la catégorie, c'est qu'on veut en ajouter une. Sinon on la modifie
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
