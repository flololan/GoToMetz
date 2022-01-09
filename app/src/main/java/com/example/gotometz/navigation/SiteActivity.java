package com.example.gotometz.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gotometz.R;
import com.example.gotometz.listeners.AddOrEditSiteFormListener;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.dao.SiteService;
import com.example.gotometz.list_views.SiteListView;
import com.example.gotometz.dbmodels.Site;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SiteActivity extends AppCompatActivity {
    private SiteService siteService;
    private CategoryService categoryService;
    private List<Site> siteList;
    private SiteListView siteListView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_site);

        // Menu initialisation
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.menu);
        bottomNavigationView.setSelectedItemId(R.id.siteMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new Menu(this));

        // Getting data from DB
        siteService = SiteService.getInstance(this);
        categoryService = CategoryService.getInstance(this);
        siteList = siteService.findAll();

        // Fill site/poi list
        siteListView = new SiteListView(this, siteList);
        listView = findViewById(R.id.siteLV);
        listView.setAdapter(siteListView);

        // add site button
        FloatingActionButton addSiteBTN = findViewById(R.id.addSiteBTN);
        addSiteBTN.setOnClickListener(new AddOrEditSiteFormListener(this, null));
    }

    // Retrieve the data of sub activity for adding and modifying
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            // If we can't get the ID of the category, it means the user wants to add one. Otherwise we edit it.
            if (data.getLongExtra("id", -1) == -1) {
                Site site = new Site(data.getStringExtra("label"), data.getDoubleExtra("latitude", 0),
                        data.getDoubleExtra("longitude", 0), data.getStringExtra("postalAddress"),
                        categoryService.findById(data.getLongExtra("categoryId", -1)), data.getStringExtra("summary"));
                site.setId(siteService.create(site));

                siteListView.add(site);
                siteListView.notifyDataSetChanged();
            } else {
                Site site = new Site(data.getLongExtra("id", -1), data.getStringExtra("label"),
                        data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0),
                        data.getStringExtra("postalAddress"), categoryService.findById(data.getLongExtra("categoryId", -1)),
                        data.getStringExtra("summary"));

                siteService.update(site);
                siteList = siteService.findAll();
                siteListView = new SiteListView(this, siteList);
                listView.setAdapter(siteListView);
            }
        }
    }
}
