package com.gooutinmetz.site.update;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;

import com.gooutinmetz.site.update.SiteForm;
import com.gooutinmetz.map.MapActivity;
import com.gooutinmetz.site.SiteModel;

public class DisplaySiteFormListener implements View.OnClickListener {
    private Activity activity;
    private SiteModel site;

    public DisplaySiteFormListener(Activity activity, SiteModel site) {
        this.activity = activity;
        this.site = site;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, SiteForm.class);
        if (site != null) {
            intent.putExtra("id", site.getId());
            intent.putExtra("label", site.getLabel());
            intent.putExtra("latitude", site.getLatitude());
            intent.putExtra("longitude", site.getLongitude());
            intent.putExtra("postalAddress", site.getPostalAddress());
            intent.putExtra("categoryId", site.getCategory().getId());
            intent.putExtra("summary", site.getSummary());
        } else if (activity instanceof MapActivity) {
            Location phonePos = ((MapActivity) activity).getUserLocation();
            if (phonePos.getLatitude() < 1){
                System.out.println("Error while getting the localisation (latitude)");
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