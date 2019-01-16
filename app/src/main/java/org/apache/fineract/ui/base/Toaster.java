package org.apache.fineract.ui.base;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.fineract.FineractApplication;
import org.apache.fineract.R;

public class Toaster {

    public static final String LOG_TAG = Toaster.class.getSimpleName();

    public static final int INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    public static final int LONG = Snackbar.LENGTH_LONG;
    public static final int SHORT = Snackbar.LENGTH_SHORT;

    private static Snackbar hideSnackbad = null;

    public static void show(View view, String text, int duration) {
        final Snackbar snackbar = Snackbar.make(view, text, duration);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(12);
        snackbar.setAction(R.string.OK, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void showProgressMessage(View view, String text, int duration) {
        final Snackbar snackbar = Snackbar.make(view, text, duration);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(12);
        snackbar.show();
        hideSnackbad = snackbar;
    }

    public static void hideSnackbar() {
        try {
            hideSnackbad.dismiss();
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }
    }

    public static void show(View view, int res, int duration) {
        show(view, FineractApplication.getContext().getResources().getString(res), duration);
    }

    public static void show(View view, String text) {
        show(view, text, Snackbar.LENGTH_LONG);
    }

    public static void show(View view, int res) {
        show(view, FineractApplication.getContext().getResources().getString(res));
    }
}