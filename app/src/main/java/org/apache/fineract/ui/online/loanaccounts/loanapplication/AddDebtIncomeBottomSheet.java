package org.apache.fineract.ui.online.loanaccounts.loanapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.CreditWorthinessFactor;
import org.apache.fineract.ui.base.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 23/07/17.
 */
public class AddDebtIncomeBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.tv_header)
    TextView tvHeader;

    @BindView(R.id.et_amount)
    EditText etAmount;

    @BindView(R.id.et_description)
    EditText etDescription;

    @BindView(R.id.btn_add_debt_income)
    Button btnAddDebtIncome;

    View rootView;

    private BottomSheetBehavior behavior;
    private OnBottomSheetDialogListener.AddDebt debtListener;
    private OnBottomSheetDialogListener.AddIncome incomeListener;
    private CreditWorthinessSource creditWorthinessSource;
    private CreditWorthinessFactor creditWorthinessFactor;

    private int position;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(), R.layout.bottom_sheet_add_debt_income, null);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) rootView.getParent());
        ButterKnife.bind(this, rootView);

        switch (creditWorthinessSource) {
            case DEBT:
                tvHeader.setText(getString(R.string.add_debt));
                btnAddDebtIncome.setText(getString(R.string.add_debt));
                break;
            case INCOME:
                tvHeader.setText(getString(R.string.add_income));
                btnAddDebtIncome.setText(getString(R.string.add_income));
                break;
            case EDIT_DEBT:
                tvHeader.setText(getString(R.string.edit_debt));
                btnAddDebtIncome.setText(getString(R.string.edit_debt));
                etAmount.setText(String.valueOf(creditWorthinessFactor.getAmount()));
                etDescription.setText(creditWorthinessFactor.getDescription());
                break;
            case EDIT_INCOME:
                tvHeader.setText(getString(R.string.edit_income));
                btnAddDebtIncome.setText(getString(R.string.edit_income));
                etAmount.setText(String.valueOf(creditWorthinessFactor.getAmount()));
                etDescription.setText(creditWorthinessFactor.getDescription());
                break;
        }

        return dialog;
    }

    @OnClick(R.id.btn_cancel)
    void onCancel() {
        dismiss();
    }

    @OnClick(R.id.btn_add_debt_income)
    void addDebtIncome() {
        if (TextUtils.isEmpty(etAmount.getText().toString().trim())) {
            Toaster.show(rootView, getString(R.string.amount_should_be_not_empty));
            return;
        }
        if (TextUtils.isEmpty(etDescription.getText().toString().trim())) {
            Toaster.show(rootView, getString(R.string.description_should_not_be_empty));
            return;
        }

        CreditWorthinessFactor creditWorthinessFactor = new CreditWorthinessFactor();
        creditWorthinessFactor.setAmount(Double.parseDouble(etAmount.getText().toString().trim()));
        creditWorthinessFactor.setDescription(etDescription.getText().toString().trim());
        switch (creditWorthinessSource) {
            case DEBT:
                debtListener.addDebt(creditWorthinessFactor);
                break;
            case INCOME:
                incomeListener.addIncome(creditWorthinessFactor);
                break;
            case EDIT_DEBT:
                debtListener.editDebt(creditWorthinessFactor, position);
                break;
            case EDIT_INCOME:
                incomeListener.editIncome(creditWorthinessFactor, position);
                break;
        }
        dismiss();
    }

    public void setCreditWorthinessSource(CreditWorthinessSource creditWorthinessSource) {
        this.creditWorthinessSource = creditWorthinessSource;
    }

    public void setCreditWorthinessFactor(CreditWorthinessFactor creditWorthinessFactor) {
        this.creditWorthinessFactor = creditWorthinessFactor;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setDebtListener(OnBottomSheetDialogListener.AddDebt debtListener) {
        this.debtListener = debtListener;
    }

    public void setIncomeListener(OnBottomSheetDialogListener.AddIncome incomeListener) {
        this.incomeListener = incomeListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
