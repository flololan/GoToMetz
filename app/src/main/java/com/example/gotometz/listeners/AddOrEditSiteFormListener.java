package com.example.gotometz.listeners;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;

import com.example.gotometz.navigation.MapsActivity;
import com.example.gotometz.form.SiteForm;
import com.example.gotometz.dbmodels.Site;

/**
 * Listener for opening the adding a site/POI form
 */
public class AddOrEditSiteFormListener implements View.OnClickListener {
    private Activity activity;
    private Site site;

    public AddOrEditSiteFormListener(Activity activity, Site site) {
        this.activity = activity;
        this.site = site;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, SiteForm.class);
        // Fill in all fields (for updates)
        if (site != null) {
            intent.putExtra("id", site.getId());
            intent.putExtra("label", site.getLabel());
            intent.putExtra("latitude", site.getLatitude());
            intent.putExtra("longitude", site.getLongitude());
            intent.putExtra("postalAddress", site.getPostalAddress());
            intent.putExtra("categoryId", site.getCategory().getId());
            intent.putExtra("summary", site.getSummary());
        }
        //If the position is available, the Longitude and Latitude will be automatically added to the form
        else if (activity instanceof MapsActivity) {
            Location phonePos = ((MapsActivity) activity).getUserLocation();
            if (phonePos.getLatitude() < 1){
                System.out.println("Localisation error!");
            }
            else{
                try {
                    intent.putExtra("latitudeForMap", phonePos.getLatitude());
                    intent.putExtra("longitudeForMap", phonePos.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
       activity.startActivityForResult(intent,1);
    }
}
