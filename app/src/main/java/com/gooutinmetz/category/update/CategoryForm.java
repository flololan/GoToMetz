package com.gooutinmetz.category.update;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gooutinmetz.R;
import com.gooutinmetz.shared.CancelFormListener;

public class CategoryForm extends AppCompatActivity {
    private TextView id;
    private EditText label;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_form);


        TextView title = findViewById(R.id.addOrUpdateCategoryTV);
        id = findViewById(R.id.categoryIdTV);
        label = findViewById(R.id.categoryLabelET);

        Button submit = findViewById(R.id.submitBTN);
        Button cancel = findViewById(R.id.cancelBTN);

        boolean isAddingACategory = this.getIntent().getExtras() != null;
        if (isAddingACategory) {
            title.setText(this.getString(R.string.addCategory));
            submit.setText(R.string.add);
        } else {
            title.setText(this.getString(R.string.updateCategory) + " " + this.getIntent().getExtras().getString("label"));
            id.setText(String.valueOf(this.getIntent().getExtras().getLong("id")));
            label.setText(this.getIntent().getExtras().getString("label"));
            submit.setText(R.string.update);
        }

        submit.setOnClickListener(new SubmitFormCategoryListener(this));
        cancel.setOnClickListener(new CancelFormListener(this));
    }

    public TextView getId() {
        return id;
    }

    public EditText getLabel() {
        return label;
    }
}
