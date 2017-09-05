package org.apache.fineract.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.apache.fineract.utils.ProgressBarHandler;

/**
 * @author Rajan Maurya
 *         On 28/07/17.
 */
public class MifosBaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private BaseActivityCallback callback;
    private ProgressBarHandler progressBarHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
