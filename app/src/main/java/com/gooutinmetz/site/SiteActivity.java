package com.gooutinmetz.site;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gooutinmetz.menu.Menu;
import com.gooutinmetz.administration.DisplaySiteFormListener;
import com.gooutinmetz.category.CategoryDAOService;
import com.gooutinmetz.R;
import com.gooutinmetz.administration.SiteListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SiteActivity extends AppCompatActivity {
    private SiteDAOService siteDAOService;
    private CategoryDAOService categoryDAOService;
    private List<SiteModel> siteList;
    private SiteListView siteListView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_site);

        // Menu
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.menu);
        bottomNavigationView.setSelectedItemId(R.id.siteMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new Menu(this));

        // Récupération des données de la base
        siteDAOService = SiteDAOService.getInstance(this);
        categoryDAOService = CategoryDAOService.getInstance(this);
        siteList = siteDAOService.findAll();

        // Remplit la liste des sites
        siteListView = new SiteListView(this, siteList);
        listView = findViewById(R.id.siteLV);
        listView.setAdapter(siteListView);

        // Bouton pour ajouter un site
        Button addSiteBTN = findViewById(R.id.addSiteBTN);
        addSiteBTN.setOnClickListener(new DisplaySiteFormListener(this, null));
    }

    // On récupère les données venant des modales d'ajout et de modification de site
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            // Si on ne récupère pas l'id du site, c'est qu'on veut en ajouter un. Sinon on le modifie
            if (data.getLongExtra("id", -1) == -1) {
                SiteModel site = new SiteModel(data.getStringExtra("label"), data.getDoubleExtra("latitude", 0),
                        data.getDoubleExtra("longitude", 0), data.getStringExtra("postalAddress"),
                        categoryDAOService.findById(data.getLongExtra("categoryId", -1)), data.getStringExtra("summary"));
                site.setId(siteDAOService.create(site));

                siteListView.add(site);
                siteListView.notifyDataSetChanged();
            } else {
                SiteModel site = new SiteModel(data.getLongExtra("id", -1), data.getStringExtra("label"),
                        data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0),
                        data.getStringExtra("postalAddress"), categoryDAOService.findById(data.getLongExtra("categoryId", -1)),
                        data.getStringExtra("summary"));

                siteDAOService.update(site);
                siteList = siteDAOService.findAll();
                siteListView = new SiteListView(this, siteList);
                listView.setAdapter(siteListView);
            }
        }
    }
}
