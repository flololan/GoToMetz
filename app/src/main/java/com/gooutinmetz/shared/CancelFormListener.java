package com.gooutinmetz.shared;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class CancelFormListener implements View.OnClickListener {
    private Activity activity;

    public CancelFormListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        activity.setResult(1, intent);
        activity.finish();
    }
}
