package com.example.gotometz.administration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gotometz.CategoryActivity;
import com.example.gotometz.R;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.dao.SiteService;
import com.example.gotometz.model.Category;

import java.util.List;

public class CategoryListView extends ArrayAdapter<Category> {
    private CategoryActivity categoryActivity;
    private CategoryService categoryDao;
    private SiteService siteDao;

    public CategoryListView(CategoryActivity categoryActivity, List<Category> categories) {
        super(categoryActivity, 0, categories);

        categoryDao = CategoryService.getInstance(categoryActivity);
        siteDao = SiteService.getInstance(categoryActivity);
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

        Category category = getItem(position);
        assert category != null;
        label.setText(category.getLabel());
        summary.setVisibility(View.GONE);
        update.setOnClickListener(new DisplayCategoryFormListener(categoryActivity, category));
        delete.setOnClickListener(new DeleteCategoryListener(categoryActivity, this, category, categoryDao, siteDao));

        return view;
    }
}
