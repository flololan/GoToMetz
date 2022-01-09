package com.gooutinmetz.map;

import android.view.View;

import androidx.annotation.NonNull;

import com.gooutinmetz.map.state.StateAddSite;
import com.gooutinmetz.map.state.StateController;
import com.gooutinmetz.map.state.StateSearchSite;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Trigger an action depending of the state of the map
 */
public class MapClickListener implements GoogleMap.OnMapClickListener, View.OnClickListener {

    public MapActivity activity;

    public StateSearchSite stateSearchSite;
    public StateAddSite stateAddSite;

    public StateController currentState;

    public MapClickListener(MapActivity activity) {
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
    public void onMapClick(@NonNull LatLng latLng) {
        this.currentState.onMapClick(latLng);
    }
}
