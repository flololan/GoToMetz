package com.example.gotometz.map;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import com.example.gotometz.MapsActivity;

public class MyLocationListener implements LocationListener {

    private MapsActivity mapsActivity;

    public MyLocationListener(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mapsActivity.setUserLocation(location);
        mapsActivity.searchSites(location);
    }
}
