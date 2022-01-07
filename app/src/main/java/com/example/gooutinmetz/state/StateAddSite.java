package com.example.gooutinmetz.state;

import android.content.Intent;
import android.view.View;

import com.example.gooutinmetz.form.SiteForm;
import com.example.gooutinmetz.map.MyMapListener;
import com.google.android.gms.maps.model.LatLng;

public class StateAddSite extends StateController {

    public StateAddSite(MyMapListener myMapListener, StateController next) {
        super(myMapListener, next);
    }

    @Override
    public void onClick(View v) {
        if (v == this.myMapListener.activity.addSiteBTN) return;

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
