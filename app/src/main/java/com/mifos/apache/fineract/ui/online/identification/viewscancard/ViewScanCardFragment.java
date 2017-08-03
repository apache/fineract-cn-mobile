package com.mifos.apache.fineract.ui.online.identification.viewscancard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.mifos.apache.fineract.utils.ImageLoaderUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 03/08/17.
 */
public class ViewScanCardFragment extends MifosBaseFragment {

    @BindView(R.id.iv_scan_card)
    ImageView ivScanCard;

    View rootView;

    private String customerIdentifier;
    private String identifierNumber;
    private String scanIdentifier;

    public static ViewScanCardFragment newInstance(String customerIdentifier,
            String identificationNumber, String scanIdentifier) {
        ViewScanCardFragment fragment = new ViewScanCardFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        args.putString(ConstantKeys.IDENTIFICATION_NUMBER, identificationNumber);
        args.putString(ConstantKeys.IDENTIFICATION_SCAN_CARD, scanIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
            identifierNumber = getArguments().getString(ConstantKeys.IDENTIFICATION_NUMBER);
            scanIdentifier = getArguments().getString(ConstantKeys.IDENTIFICATION_SCAN_CARD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_scan_card, container, false);
        ButterKnife.bind(this, rootView);

        ivScanCard.setVisibility(View.VISIBLE);
        ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils(getActivity());
        imageLoaderUtils.loadImage(imageLoaderUtils.buildIdentificationScanCardImageUrl(
                customerIdentifier, identifierNumber, scanIdentifier), ivScanCard);

        return rootView;
    }
}
