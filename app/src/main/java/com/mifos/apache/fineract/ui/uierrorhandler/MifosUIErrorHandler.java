package com.mifos.apache.fineract.ui.uierrorhandler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;

import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 03/09/17.
 */
public class MifosUIErrorHandler {

    private ImageView ivEmptyFeatureImage;
    private TextView tvFeatureName;
    private TextView tvSubFeatureName;
    private LinearLayout llEmptyUI;
    private LinearLayout llErrorToLoad;
    private TextView tvErrorFeatureName;

    private Context context;
    private View view;

    public MifosUIErrorHandler(Context context, View view) {
        this.context = context;
        this.view = view;
        initializeUI();
    }

    public void showEmptyOrErrorUI(UIType errorType, String featureName,
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
                llErrorToLoad.setVisibility(View.VISIBLE);
                tvErrorFeatureName.setText(featureName);
                llEmptyUI.setVisibility(View.GONE);
                break;
        }
    }

    private void initializeUI() {
        ivEmptyFeatureImage = ButterKnife.findById(view, R.id.iv_empty_feature_image);
        tvFeatureName = ButterKnife.findById(view, R.id.tv_empty_feature_name);
        tvSubFeatureName = ButterKnife.findById(view, R.id.tv_empty_sub_feature_name);
        llEmptyUI = ButterKnife.findById(view, R.id.ll_empty_ui);
        llErrorToLoad = ButterKnife.findById(view, R.id.ll_error_to_load);
        tvErrorFeatureName = ButterKnife.findById(view, R.id.tv_error_feature_name);
    }
}
