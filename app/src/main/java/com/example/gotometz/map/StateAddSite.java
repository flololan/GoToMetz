package com.example.gotometz.map;

import android.content.Intent;
import android.view.View;

import com.example.gotometz.form.SiteForm;
import com.example.gotometz.listeners.MyMapListener;
import com.google.android.gms.maps.model.LatLng;

/**
 * Logic for when clicking on map when AddState is active
 */
public class StateAddSite extends StateController {

    public StateAddSite(MyMapListener myMapListener, StateController next) {
        super(myMapListener, next);
    }

    @Override
    public void onClick(View v) {
        if (v == this.myMapListener.activity.mapAddBTN) return;

        this.myMapListener.currentState = this.next;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(myMapListener.activity, SiteForm.class);
        intent.putExtra("latitudeForMap", latLng.latitude);
        intent.putExtra("longitudeForMap", latLng.longitude);
        myMapListener.activity.startActivityForResult(intent, 1);
    }
}
