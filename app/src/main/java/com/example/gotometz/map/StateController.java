package com.example.gotometz.map;

import android.view.View;

import com.example.gotometz.listeners.MyMapListener;
import com.google.android.gms.maps.model.LatLng;

/**
 * Abstract class which manages the common features for onMap feature (AddSite, SearchSite)
 */
public abstract class StateController {

    public MyMapListener myMapListener;
    public StateController next;

    public StateController(MyMapListener myMapListener, StateController next) {
        this.myMapListener = myMapListener;
        this.next = next;
    }

    public abstract void onClick(View v);

    public abstract void onMapClick(LatLng latLng);
}
