package com.gooutinmetz.map.state;

import android.content.Intent;
import android.view.View;

import com.gooutinmetz.form.SiteForm;
import com.gooutinmetz.map.MapClickListener;
import com.google.android.gms.maps.model.LatLng;

public class StateAddSite extends StateController {

    public StateAddSite(MapClickListener mapClickListener, StateController next) {
        super(mapClickListener, next);
    }

    @Override
    public void onClick(View v) {
        if (v == this.mapClickListener.activity.addSiteBTN) return;

        this.mapClickListener.currentState = this.next;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(mapClickListener.activity, SiteForm.class);
        intent.putExtra("latitudeForMap", latLng.latitude);
        intent.putExtra("longitudeForMap", latLng.longitude);
        mapClickListener.activity.startActivityForResult(intent, 1);
    }
}
