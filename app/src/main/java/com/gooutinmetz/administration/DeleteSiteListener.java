package com.gooutinmetz.administration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.gooutinmetz.SiteActivity;
import com.gooutinmetz.dao.SiteService;
import com.example.gooutinmetz.R;
import com.gooutinmetz.model.Site;

public class DeleteSiteListener implements View.OnClickListener {
    private Site site;
    private SiteListView siteListView;
    private SiteService siteService;
    private SiteActivity siteActivity;

    public DeleteSiteListener(SiteActivity siteActivity, SiteListView siteListView, Site site, SiteService siteService){
        this.siteActivity = siteActivity;
        this.site = site;
        this.siteListView = siteListView;
        this.siteService = siteService;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(siteActivity);
        builder.setMessage(siteActivity.getString(R.string.deleteSite)).setTitle(R.string.delete);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                siteService.delete(site.getId());
                siteListView.remove(site);
                siteListView.notifyDataSetChanged();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
