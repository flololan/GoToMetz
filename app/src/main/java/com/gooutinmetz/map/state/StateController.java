package com.gooutinmetz.map.state;

import android.view.View;

import com.gooutinmetz.map.MapClickListener;
import com.google.android.gms.maps.model.LatLng;

public abstract class StateController {

    public MapClickListener mapClickListener;
    public StateController next;

    public StateController(MapClickListener mapClickListener, StateController next) {
        this.mapClickListener = mapClickListener;
        this.next = next;
    }

    public abstract void onClick(View v);

    public abstract void onMapClick(LatLng latLng);
}
