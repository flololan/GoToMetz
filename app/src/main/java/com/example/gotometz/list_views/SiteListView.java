package com.example.gotometz.list_views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gotometz.R;
import com.example.gotometz.navigation.SiteActivity;
import com.example.gotometz.dao.SiteService;
import com.example.gotometz.listeners.DeleteSiteListener;
import com.example.gotometz.listeners.AddOrEditSiteFormListener;
import com.example.gotometz.dbmodels.Site;

import java.util.List;
/**
 * Sub-view of site/POI list to be added in activities
 */
public class SiteListView extends ArrayAdapter<Site> {
    private SiteActivity siteActivity;
    private SiteService siteService;

    public SiteListView(SiteActivity siteActivity, List<Site> sites) {
        super(siteActivity, 0, sites);
        siteService = SiteService.getInstance(siteActivity);
        this.siteActivity = siteActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(siteActivity).inflate(R.layout.list_of_item_view, parent, false);
        Site site = getItem(position);

        TextView label = view.findViewById(R.id.itemLabelTV);
        TextView summary = view.findViewById(R.id.itemSummaryTV);
        Button update = view.findViewById(R.id.updateItemBTN);
        Button delete = view.findViewById(R.id.deleteItemBTN);
        if (site != null) {
            label.setText(site.getLabel());
            summary.setText(site.getSummary());
            update.setOnClickListener(new AddOrEditSiteFormListener(siteActivity, site));
            delete.setOnClickListener(new DeleteSiteListener(siteActivity, this, site, siteService));
        }

        return view;
    }
}
