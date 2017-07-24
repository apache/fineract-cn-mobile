package com.mifos.apache.fineract.ui.loanapplication.loandocument;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.loanapplication.OnBottomSheetDialogListener;

import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 24/07/17.
 */

public class AddDocumentBottomSheet extends BottomSheetDialogFragment {

    View rootView;

    private BottomSheetBehavior behavior;
    private Document document;
    private OnBottomSheetDialogListener.AddDocument addDocumentListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(), R.layout.bottom_sheet_add_document, null);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) rootView.getParent());
        ButterKnife.bind(this, rootView);

        switch (document) {
            case ADD:

                break;
            case EDIT:
                break;
        }

        return dialog;
    }

    public void setAddDocumentListener(OnBottomSheetDialogListener.AddDocument listener) {
        addDocumentListener = listener;
    }

}
