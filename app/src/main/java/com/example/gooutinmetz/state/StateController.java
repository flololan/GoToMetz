package com.example.gooutinmetz.state;

import android.view.View;

import com.example.gooutinmetz.map.MyMapListener;
import com.google.android.gms.maps.model.LatLng;

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
