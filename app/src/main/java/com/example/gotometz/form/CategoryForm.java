package com.example.gotometz.form;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gotometz.R;
import com.example.gotometz.listeners.CancelFormListener;
import com.example.gotometz.listeners.SubmitFormCategoryListener;

/**
 * Fragment of category form to be added in activities
 */
public class CategoryForm extends AppCompatActivity {
    private TextView id;
    private EditText label;
    private boolean isAdding = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_form);

        // If we don't find values we are in editing mode
        if (this.getIntent().getExtras() != null)
            isAdding = false;

        TextView title = findViewById(R.id.addOrUpdateCategoryTV);
        id = findViewById(R.id.categoryIdTV);
        label = findViewById(R.id.categoryLabelET);

        Button submit = findViewById(R.id.submitBTN);
        Button cancel = findViewById(R.id.cancelBTN);

        if (isAdding) {
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
