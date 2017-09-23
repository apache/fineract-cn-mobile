package org.apache.fineract.ui.uierrorhandler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.fineract.R;

import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 03/09/17.
 */
public class FineractUIErrorHandler {

    private ImageView ivEmptyFeatureImage;
    private TextView tvFeatureName;
    private TextView tvSubFeatureName;
    private Button btnTryAgain;
    private LinearLayout llEmptyUI;
    private LinearLayout llErrorToLoad;
    private LinearLayout llNoInternet;
    private LinearLayout llError;
    private TextView tvErrorFeatureName;

    private Context context;
    private View view;

    public FineractUIErrorHandler(Context context, View view) {
        this.context = context;
        this.view = view;
        initializeUI();
    }

    public void showEmptyOrErrorOrNoConnectionUI(UIType errorType, String featureName,
            @Nullable String subFeatureName, @Nullable Integer featureImage) {
        switch (errorType) {
            case EMPTY_UI:
                llEmptyUI.setVisibility(View.VISIBLE);
                ivEmptyFeatureImage.setImageResource(featureImage);
                tvFeatureName.setText(context.getString(R.string.empty_ui_message, featureName));
                tvSubFeatureName.setText(
                        context.getString(R.string.empty_ui_sub_message, subFeatureName));
                llErrorToLoad.setVisibility(View.GONE);
                break;
            case ERROR_UI:
                llEmptyUI.setVisibility(View.GONE);
                llNoInternet.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
                llErrorToLoad.setVisibility(View.VISIBLE);
                btnTryAgain.setText(context.getString(R.string.try_again));
                tvErrorFeatureName.setText(featureName);
                break;
            case NO_INTERNET:
                llEmptyUI.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);
                llNoInternet.setVisibility(View.VISIBLE);
                llErrorToLoad.setVisibility(View.VISIBLE);
                btnTryAgain.setText(context.getString(R.string.retry));
                break;
        }
    }

    private void initializeUI() {
        ivEmptyFeatureImage = ButterKnife.findById(view, R.id.iv_empty_feature_image);
        tvFeatureName = ButterKnife.findById(view, R.id.tv_empty_feature_name);
        tvSubFeatureName = ButterKnife.findById(view, R.id.tv_empty_sub_feature_name);
        llEmptyUI = ButterKnife.findById(view, R.id.ll_empty_ui);
        llErrorToLoad = ButterKnife.findById(view, R.id.ll_error_to_load);
        llNoInternet = ButterKnife.findById(view, R.id.ll_no_internet);
        llError = ButterKnife.findById(view, R.id.ll_error);
        btnTryAgain = ButterKnife.findById(view, R.id.btn_try_again);
        tvErrorFeatureName = ButterKnife.findById(view, R.id.tv_error_feature_name);
    }
}
