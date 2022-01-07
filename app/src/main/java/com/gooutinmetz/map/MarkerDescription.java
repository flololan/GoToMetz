package com.gooutinmetz.map;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gooutinmetz.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerDescription implements GoogleMap.InfoWindowAdapter {
    private View markerModal = null;
    private final LayoutInflater inflater;

    public MarkerDescription(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        if (markerModal == null)
            markerModal = inflater.inflate(R.layout.marker_description, null);

        TextView title = markerModal.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = markerModal.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());

        return markerModal;
    }
}
