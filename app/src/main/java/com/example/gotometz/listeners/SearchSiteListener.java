package com.example.gotometz.listeners;

import android.view.View;

import com.example.gotometz.map.SearchSiteDialog;
import com.example.gotometz.navigation.MapsActivity;

/**
 * Opens Search Site dialog
 */
public class SearchSiteListener implements View.OnClickListener {

    private MapsActivity activity;

    public SearchSiteListener(MapsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        SearchSiteDialog searchSiteDialog = new SearchSiteDialog(activity, activity.getUserLocation());
        searchSiteDialog.show(activity.getSupportFragmentManager(), "Search");
    }
}
