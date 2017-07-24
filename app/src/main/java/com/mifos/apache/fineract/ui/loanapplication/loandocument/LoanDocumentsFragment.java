package com.mifos.apache.fineract.ui.loanapplication.loandocument;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.loanapplication.OnBottomSheetDialogListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * @author Rajan Maurya
 *         On 19/07/17.
 */
public class LoanDocumentsFragment extends MifosBaseFragment implements Step, LoanDocumentContract,
        OnBottomSheetDialogListener.AddDocument {

    View rootView;

    public static LoanDocumentsFragment newInstance() {
        LoanDocumentsFragment fragment = new LoanDocumentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_document, container, false);

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

    @Override
    public void showDocumentBottomSheet(Document document) {
        AddDocumentBottomSheet addDocumentBottomSheet = new AddDocumentBottomSheet();
        addDocumentBottomSheet.setAddDocumentListener(this);
    }

    @Override
    public void addDocument() {

    }
}
