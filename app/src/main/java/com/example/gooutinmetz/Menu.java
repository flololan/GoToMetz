package com.example.gooutinmetz;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.gooutinmetz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menu implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private Intent mapIntent, categoryIntent, siteIntent;

    public Menu(Context context) {
        this.context = context;

        mapIntent = new Intent(this.context, MapsActivity.class);
        siteIntent = new Intent(this.context, SiteActivity.class);
        categoryIntent = new Intent(this.context, CategoryActivity.class);
    }

    // Quand on change d'activité dans le menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapMenu:
                this.context.startActivity(mapIntent);
                break;

            case R.id.siteMenu:
                this.context.startActivity(siteIntent);
                break;

            case R.id.categoryMenu:
                this.context.startActivity(categoryIntent);
                break;

            default:
                break;
        }

        return true;
    }
}
