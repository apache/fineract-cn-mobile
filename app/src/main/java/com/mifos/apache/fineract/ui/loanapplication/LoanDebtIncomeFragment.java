package com.mifos.apache.fineract.ui.loanapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * @author Rajan Maurya
 *         On 19/07/17.
 */

public class LoanDebtIncomeFragment extends MifosBaseFragment implements Step {

    //private OnNavigationBarListener onNavigationBarListener;

    View rootView;

    public static LoanDebtIncomeFragment newInstance() {
        LoanDebtIncomeFragment fragment = new LoanDebtIncomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_debt_income_ratio, container, false);

        return rootView;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (OnNavigationBarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener");
        }
    }*/
}
