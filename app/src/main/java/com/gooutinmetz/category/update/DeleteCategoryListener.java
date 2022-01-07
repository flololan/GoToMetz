package com.gooutinmetz.category.update;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.gooutinmetz.category.CategoryListView;
import com.gooutinmetz.category.CategoryDaoService;
import com.gooutinmetz.site.SiteDaoService;
import com.gooutinmetz.category.CategoryActivity;
import com.gooutinmetz.R;
import com.gooutinmetz.category.CategoryModel;

public class DeleteCategoryListener implements View.OnClickListener {
    private CategoryModel category;
    private CategoryListView categoryListView;
    private CategoryDaoService categoryDAOService;
    private SiteDaoService siteDAOService;
    private CategoryActivity categoryActivity;

    public DeleteCategoryListener(CategoryActivity activity, CategoryListView categoryListView, CategoryModel category, CategoryDaoService categoryDAOService, SiteDaoService siteDAOService) {
        this.categoryActivity = activity;
        this.category = category;
        this.siteDAOService = siteDAOService;
        this.categoryListView = categoryListView;
        this.categoryDAOService = categoryDAOService;
    }

    @Override
    public void onClick(View v) {
        // On ne veut pas supprimer une catégorie qui est utilisée par un site
        if (siteDAOService.isCategoryUsed(category)) {
            Toast.makeText(categoryActivity, R.string.warningDelete, Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(categoryActivity);
            builder.setMessage(categoryActivity.getString(R.string.deleteCategory)).setTitle(R.string.delete);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    categoryDAOService.delete(category.getId());
                    categoryListView.remove(category);
                    categoryListView.notifyDataSetChanged();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
