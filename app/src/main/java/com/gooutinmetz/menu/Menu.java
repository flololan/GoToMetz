package com.gooutinmetz.menu;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.gooutinmetz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gooutinmetz.category.CategoryActivity;
import com.gooutinmetz.map.MapActivity;
import com.gooutinmetz.site.SiteActivity;

public class Menu implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final Context context;
    private final Intent mapIntent;
    private final Intent categoryIntent;
    private final Intent siteIntent;

    public Menu(Context context) {
        this.context = context;

        mapIntent = new Intent(this.context, MapActivity.class);
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
