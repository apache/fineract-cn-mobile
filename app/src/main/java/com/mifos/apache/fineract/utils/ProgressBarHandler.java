package com.mifos.apache.fineract.utils;

import android.R.attr;
import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


/**
 * @author Rajan Maurya
 */
public class ProgressBarHandler {

    private ProgressBar progressBar;
    private Context context;

    public ProgressBarHandler(Context context) {
        this.context = context;

        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(id.content).getRootView();

        progressBar = new ProgressBar(context, null, attr.progressBarStyleInverse);
        progressBar.setIndeterminate(true);

        LayoutParams params = new
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(context);

        rl.setGravity(Gravity.CENTER);
        rl.addView(progressBar);

        layout.addView(rl, params);

        hide();
    }

    public void show() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hide() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}