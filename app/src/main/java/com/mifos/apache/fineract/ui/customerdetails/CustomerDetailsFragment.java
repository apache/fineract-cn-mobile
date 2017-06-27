package com.mifos.apache.fineract.ui.customerdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.utils.ConstantKeys;

import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 26/06/17.
 */
public class CustomerDetailsFragment extends MifosBaseFragment {

    private View rootView;
    private String customerIdentifier;

    public static CustomerDetailsFragment newInstance(String identifier) {
        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, identifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_details, container, false);
        ButterKnife.bind(this, rootView);


        return rootView;
    }
}
