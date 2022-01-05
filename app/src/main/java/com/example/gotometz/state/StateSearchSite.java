package com.example.gotometz.state;

import android.location.Location;
import android.view.View;

import com.example.gotometz.map.SearchSiteDialog;
import com.example.gotometz.map.MyMapListener;
import com.google.android.gms.maps.model.LatLng;

public class StateSearchSite extends StateController {

    public StateSearchSite(MyMapListener myMapListener, StateController next) {
        super(myMapListener, next);
    }

    @Override
    public void onClick(View v) {
        if (v == this.myMapListener.activity.searchSiteBTN) return;

        this.myMapListener.currentState = this.next;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        SearchSiteDialog searchSiteDialog = new SearchSiteDialog(myMapListener.activity, location);
        searchSiteDialog.show(myMapListener.activity.getSupportFragmentManager(), "search");
    }
}
