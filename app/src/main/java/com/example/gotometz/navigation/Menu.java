package com.example.gotometz.navigation;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.gotometz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class Menu implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private Intent mapIntent, categoryIntent, siteIntent;
    private Map<Integer, Intent> activityMap;

    public Menu(Context context) {
        this.context = context;

        mapIntent = new Intent(this.context, MapsActivity.class);
        siteIntent = new Intent(this.context, SiteActivity.class);
        categoryIntent = new Intent(this.context, CategoryActivity.class);
        this.registerActivities();
    }

    /**
     * Registering activities to add to HashMap
     */
    private void registerActivities(){
        activityMap = new HashMap();
        activityMap.put(R.id.mapMenu, mapIntent);
        activityMap.put(R.id.siteMenu, siteIntent);
        activityMap.put(R.id.categoryMenu, categoryIntent);
    }

    /** Start Activity depending on selected option in Menu
     *  Note: I would have used a simple switch here but we were told not to use switches
     * @param item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.context.startActivity(this.activityMap.get(item.getItemId()));
        return true;
    }
}
