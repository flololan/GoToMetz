package com.example.gotometz.listeners;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import com.example.gotometz.navigation.MapsActivity;
import com.example.gotometz.map.StateAddSite;
import com.example.gotometz.map.StateController;
import com.example.gotometz.map.StateSearchSite;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Checks if user tapped the map, depending on state will call AddSite or SearchSite
 */
public class MyMapListener implements GoogleMap.OnMapClickListener, View.OnClickListener {

    public MapsActivity activity;

    public StateSearchSite stateSearchSite;
    public StateAddSite stateAddSite;

    public StateController currentState;

    public MyMapListener(MapsActivity activity) {
        this.activity = activity;

        activity.mapSearchBTN.setOnClickListener(this);
        activity.mapAddBTN.setOnClickListener(this);
        this.activity.mMap.setOnMapClickListener(this);

        this.stateInitialization();
    }

    public void stateInitialization() {
        this.stateAddSite = new StateAddSite(this, null);
        this.stateSearchSite = new StateSearchSite(this, this.stateAddSite);
        this.stateAddSite.next = this.stateSearchSite;

        this.currentState = this.stateSearchSite;
        toggleColor();


    }

    @Override
    public void onClick(View v) {
        this.currentState.onClick(v);
        toggleColor();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.currentState.onMapClick(latLng);
    }

    public void toggleColor(){
        if (this.currentState == this.stateSearchSite){
            activity.mapSearchBTN.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2d7cbb")));
        }
        else{
            activity.mapSearchBTN.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03dac5")));
        }
        if (this.currentState == this.stateAddSite){
            activity.mapAddBTN.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2d7cbb")));
        }
        else{
            activity.mapAddBTN.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03dac5")));
        }
    }
}
