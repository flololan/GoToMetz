package com.gooutinmetz.administration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gooutinmetz.SiteActivity;
import com.gooutinmetz.dao.SiteService;
import com.example.gooutinmetz.R;
import com.gooutinmetz.model.Site;

import java.util.List;

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
            view = LayoutInflater.from(siteActivity).inflate(R.layout.items_view, parent, false);
        Site site = getItem(position);

        TextView label = view.findViewById(R.id.itemLabelTV);
        TextView summary = view.findViewById(R.id.itemSummaryTV);
        Button update = view.findViewById(R.id.updateItemBTN);
        Button delete = view.findViewById(R.id.deleteItemBTN);
        if (site != null) {
            label.setText(site.getLabel());
            summary.setText(site.getSummary());
            update.setOnClickListener(new DisplaySiteFormListener(siteActivity, site));
            delete.setOnClickListener(new DeleteSiteListener(siteActivity, this, site, siteService));
        }

        return view;
    }
}
