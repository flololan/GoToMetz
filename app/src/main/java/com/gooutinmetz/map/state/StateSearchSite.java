package com.gooutinmetz.map.state;

import android.location.Location;
import android.view.View;

import com.gooutinmetz.map.MapClickListener;
import com.gooutinmetz.map.SearchSiteDialog;
import com.google.android.gms.maps.model.LatLng;

public class StateSearchSite extends StateController {

    public StateSearchSite(MapClickListener mapClickListener, StateController next) {
        super(mapClickListener, next);
    }

    @Override
    public void onClick(View v) {
        if (v == this.mapClickListener.activity.searchSiteBTN) return;

        this.mapClickListener.currentState = this.next;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        SearchSiteDialog searchSiteDialog = new SearchSiteDialog(mapClickListener.activity, location);
        searchSiteDialog.show(mapClickListener.activity.getSupportFragmentManager(), "search");
    }
}
