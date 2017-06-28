package com.mifos.apache.fineract.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mifos.apache.fineract.utils.ProgressBarHandler;

public class MifosBaseFragment extends Fragment {

    private BaseActivityCallback callback;
    private ProgressBarHandler progressBarHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBarHandler = new ProgressBarHandler(getActivity());
    }

    protected void showMifosProgressDialog(String message) {
        if (callback != null) {
            callback.showMifosProgressDialog(message);
        }
    }

    protected void hideMifosProgressDialog() {
        if (callback != null) {
            callback.hideMifosProgressDialog();
        }
    }

    protected void setToolbarTitle(String title) {
        callback.setToolbarTitle(title);
    }

    protected void showMifosProgressBar() {
        progressBarHandler.show();
    }

    protected void hideMifosProgressBar() {
        progressBarHandler.hide();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            callback = (BaseActivityCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BaseActivityCallback methods");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

}