package com.gooutinmetz.administration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gooutinmetz.category.CategoryDAOService;
import com.gooutinmetz.site.SiteDAOService;
import com.gooutinmetz.category.CategoryActivity;
import com.gooutinmetz.R;
import com.gooutinmetz.category.CategoryModel;

import java.util.List;

public class CategoryListView extends ArrayAdapter<CategoryModel> {
    private CategoryActivity categoryActivity;
    private CategoryDAOService categoryDao;
    private SiteDAOService siteDao;

    public CategoryListView(CategoryActivity categoryActivity, List<CategoryModel> categories) {
        super(categoryActivity, 0, categories);

        categoryDao = CategoryDAOService.getInstance(categoryActivity);
        siteDao = SiteDAOService.getInstance(categoryActivity);
        this.categoryActivity = categoryActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(categoryActivity).inflate(R.layout.items_view, parent, false);
        TextView label = view.findViewById(R.id.itemLabelTV);
        TextView summary = view.findViewById(R.id.itemSummaryTV);
        Button update = view.findViewById(R.id.updateItemBTN);
        Button delete = view.findViewById(R.id.deleteItemBTN);

        CategoryModel category = getItem(position);
        assert category != null;
        label.setText(category.getLabel());
        summary.setVisibility(View.GONE);
        update.setOnClickListener(new DisplayCategoryFormListener(categoryActivity, category));
        delete.setOnClickListener(new DeleteCategoryListener(categoryActivity, this, category, categoryDao, siteDao));

        return view;
    }
}
