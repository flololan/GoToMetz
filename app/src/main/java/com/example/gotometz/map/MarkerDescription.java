package com.example.gotometz.map;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.gotometz.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerDescription implements GoogleMap.InfoWindowAdapter {
    private View markerModal = null;
    private LayoutInflater inflater;

    public MarkerDescription(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (markerModal == null)
            markerModal = inflater.inflate(R.layout.marker_description, null);

        TextView title = markerModal.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = markerModal.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());

        return markerModal;
    }
}
