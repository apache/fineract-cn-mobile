package org.apache.fineract.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.apache.fineract.ui.uierrorhandler.FineractUIErrorHandler;
import org.apache.fineract.ui.uierrorhandler.UIType;
import org.apache.fineract.utils.ProgressBarHandler;

public class FineractBaseFragment extends Fragment {

    private BaseActivityCallback callback;
    private ProgressBarHandler progressBarHandler;
    private FineractUIErrorHandler fineractUIErrorHandler;

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

    public void hideKeyboard(View view, Context context) {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager
                .RESULT_UNCHANGED_SHOWN);
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

    protected void initializeFineractUIErrorHandler(Context context, View view) {
        fineractUIErrorHandler = new FineractUIErrorHandler(context, view);
    }

    protected void showFineractEmptyUI(@NonNull String featureName, @Nullable String subFeatureName,
            @NonNull Integer featureImage) {
        fineractUIErrorHandler.showEmptyOrErrorOrNoConnectionUI(UIType.EMPTY_UI, featureName,
                subFeatureName, featureImage);
    }

    protected void showFineractErrorUI(@NonNull String featureName) {
        fineractUIErrorHandler.showEmptyOrErrorOrNoConnectionUI(UIType.ERROR_UI, featureName, null,
                null);
    }

    protected void showFineractNoInternetUI() {
        fineractUIErrorHandler.showEmptyOrErrorOrNoConnectionUI(UIType.NO_INTERNET, null, null,
                null);
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