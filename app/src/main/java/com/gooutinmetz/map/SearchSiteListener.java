package com.gooutinmetz.map;

import android.view.View;

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
