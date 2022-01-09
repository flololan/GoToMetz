package com.example.gotometz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class Menu implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private Intent mapIntent, categoryIntent, siteIntent;
    private Map<Integer, Intent> activities;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("NewApi")
    public Menu(Context context) {
        this.context = context;

        mapIntent = new Intent(this.context, MapsActivity.class);
        siteIntent = new Intent(this.context, SiteActivity.class);
        categoryIntent = new Intent(this.context, CategoryActivity.class);

        this.registerActivities();
    }

    private void registerActivities() {
        activities = new HashMap();
        activities.put(R.id.mapMenu, mapIntent);
        activities.put(R.id.siteMenu, siteIntent);
        activities.put(R.id.categoryMenu, categoryIntent);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.context.startActivity(this.activities.get(item.getItemId()));
        return true;
    }
}
