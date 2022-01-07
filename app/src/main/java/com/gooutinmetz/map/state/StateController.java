package com.gooutinmetz.map.state;

import android.view.View;

import com.gooutinmetz.map.MyMapListener;
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
