package com.example.gooutinmetz.map;

import android.view.View;

import com.example.gooutinmetz.MapsActivity;
import com.example.gooutinmetz.state.StateAddSite;
import com.example.gooutinmetz.state.StateController;
import com.example.gooutinmetz.state.StateSearchSite;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MyMapListener implements GoogleMap.OnMapClickListener, View.OnClickListener {

    public MapsActivity activity;

    public StateSearchSite stateSearchSite;
    public StateAddSite stateAddSite;

    public StateController currentState;

    public MyMapListener(MapsActivity activity) {
        this.activity = activity;

        activity.searchSiteBTN.setOnClickListener(this);
        activity.addSiteBTN.setOnClickListener(this);
        this.activity.mMap.setOnMapClickListener(this);

        this.stateInitialization();
    }

    public void stateInitialization() {
        this.stateAddSite = new StateAddSite(this, null);
        this.stateSearchSite = new StateSearchSite(this, this.stateAddSite);
        this.stateAddSite.next = this.stateSearchSite;

        this.currentState = this.stateSearchSite;
    }

    @Override
    public void onClick(View v) {
        this.currentState.onClick(v);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.currentState.onMapClick(latLng);
    }
}
