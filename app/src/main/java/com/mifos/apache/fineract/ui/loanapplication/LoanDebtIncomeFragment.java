package com.mifos.apache.fineract.ui.loanapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.loan.CreditWorthinessFactor;
import com.mifos.apache.fineract.ui.adapters.LoanDebtIncomeAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 19/07/17.
 */
public class LoanDebtIncomeFragment extends MifosBaseFragment implements Step,
        OnBottomSheetDialogListener.AddDebt, OnBottomSheetDialogListener.AddIncome,
        LoanDebtIncomeAdapter.OnClickEditDeleteListener {

    public static final String LOG_TAG = LoanDebtIncomeFragment.class.getSimpleName();

    @BindView(R.id.rv_debt)
    RecyclerView rvDebt;

    @BindView(R.id.rv_income)
    RecyclerView rvIncome;

    @BindView(R.id.tv_debt_income_ratio)
    TextView tvDebtIncomeRatio;

    @BindView(R.id.tv_total_debt)
    TextView tvTotalDebt;

    @BindView(R.id.tv_total_income)
    TextView tvTotalIncome;

    @BindView(R.id.tv_empty_debt_list)
    TextView tvEmptyDebtList;

    @BindView(R.id.tv_empty_income_list)
    TextView tvEmptyIncomeList;

    @Inject
    LoanDebtIncomeAdapter debtAdapter;

    @Inject
    LoanDebtIncomeAdapter incomeAdapter;

    private OnNavigationBarListener.LoanDebtIncomeData onNavigationBarListener;

    View rootView;

    private Double totalDebts = 0.0;
    private Double totalIncome = 0.0;
    private Double ratio = 00.00;

    private List<CreditWorthinessFactor> debtCreditWorthinessFactors;
    private List<CreditWorthinessFactor> incomeCreditWorthinessFactors;

    public static LoanDebtIncomeFragment newInstance() {
        LoanDebtIncomeFragment fragment = new LoanDebtIncomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        debtCreditWorthinessFactors = new ArrayList<>();
        incomeCreditWorthinessFactors = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_debt_income_ratio, container, false);
        ButterKnife.bind(this, rootView);

        showUserInterface();

        return rootView;
    }

    @OnClick(R.id.btn_add_debt)
    void addDebt() {
        showDebtIncomeBottomSheet(CreditWorthinessSource.DEBT, null, null);
    }

    @OnClick(R.id.btn_add_income)
    void addIncome() {
        showDebtIncomeBottomSheet(CreditWorthinessSource.INCOME, null, null);
    }

    void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDebt.setLayoutManager(layoutManager);
        rvDebt.setHasFixedSize(true);
        debtAdapter.setCreditWorthinessSource(CreditWorthinessSource.DEBT);
        debtAdapter.setOnClickEditDeleteListener(this);
        debtAdapter.setCreditWorthinessFactors(debtCreditWorthinessFactors);
        rvDebt.setAdapter(debtAdapter);

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity());
        layoutManagerIncome.setOrientation(LinearLayoutManager.VERTICAL);
        rvIncome.setLayoutManager(layoutManagerIncome);
        rvIncome.setHasFixedSize(true);
        incomeAdapter.setCreditWorthinessSource(CreditWorthinessSource.INCOME);
        incomeAdapter.setOnClickEditDeleteListener(this);
        incomeAdapter.setCreditWorthinessFactors(incomeCreditWorthinessFactors);
        rvIncome.setAdapter(incomeAdapter);

        tvTotalDebt.setText(getString(R.string.total_debt, setPrecision(totalDebts)));
        tvTotalIncome.setText(getString(R.string.total_income, setPrecision(totalIncome)));
        tvDebtIncomeRatio.setText(getString(R.string.ratio, setPrecision(ratio)));
    }

    @Override
    public VerificationError verifyStep() {
        onNavigationBarListener.setDebtIncome(debtCreditWorthinessFactors,
                incomeCreditWorthinessFactors);
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (OnNavigationBarListener.LoanDebtIncomeData) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener.LoanDebtIncomeData");
        }
    }

    @Override
    public void addDebt(CreditWorthinessFactor creditWorthinessFactor) {
        if (debtAdapter.getItemCount() == 0) {
            rvDebt.setVisibility(View.VISIBLE);
            tvEmptyDebtList.setVisibility(View.GONE);
        }
        debtCreditWorthinessFactors.add(creditWorthinessFactor);
        debtAdapter.notifyDataSetChanged();
        updateDebtsAndRatio();
    }

    @Override
    public void editDebt(CreditWorthinessFactor creditWorthinessFactor, int position) {
        debtCreditWorthinessFactors.set(position, creditWorthinessFactor);
        debtAdapter.notifyDataSetChanged();
        updateDebtsAndRatio();
    }

    @Override
    public void addIncome(CreditWorthinessFactor creditWorthinessFactor) {
        if (incomeAdapter.getItemCount() == 0) {
            rvIncome.setVisibility(View.VISIBLE);
            tvEmptyIncomeList.setVisibility(View.GONE);
        }
        incomeCreditWorthinessFactors.add(creditWorthinessFactor);
        incomeAdapter.notifyDataSetChanged();
        updateIncomeAndRatio();
    }

    @Override
    public void editIncome(CreditWorthinessFactor creditWorthinessFactor, int position) {
        incomeCreditWorthinessFactors.set(position, creditWorthinessFactor);
        incomeAdapter.notifyDataSetChanged();
        updateIncomeAndRatio();
    }

    @Override
    public void onClickEdit(CreditWorthinessSource creditWorthinessSource, int position) {
        switch (creditWorthinessSource) {
            case DEBT:
                showDebtIncomeBottomSheet(CreditWorthinessSource.EDIT_DEBT,
                        debtCreditWorthinessFactors.get(position), position);
                break;
            case INCOME:
                showDebtIncomeBottomSheet(CreditWorthinessSource.EDIT_INCOME,
                        incomeCreditWorthinessFactors.get(position), position);
                break;
        }
    }

    @Override
    public void onClickDelete(CreditWorthinessSource creditWorthinessSource, int position) {
        switch (creditWorthinessSource) {
            case DEBT:
                debtCreditWorthinessFactors.remove(position);
                debtAdapter.notifyDataSetChanged();
                if (debtAdapter.getItemCount() == 0) {
                    tvEmptyDebtList.setVisibility(View.VISIBLE);
                    rvDebt.setVisibility(View.GONE);
                }
                updateDebtsAndRatio();
                break;
            case INCOME:
                incomeCreditWorthinessFactors.remove(position);
                incomeAdapter.notifyDataSetChanged();
                if (incomeAdapter.getItemCount() == 0) {
                    tvEmptyIncomeList.setVisibility(View.VISIBLE);
                    rvIncome.setVisibility(View.GONE);
                }
                updateIncomeAndRatio();
                break;
        }
    }

    public void showDebtIncomeBottomSheet(CreditWorthinessSource creditWorthinessSource,
            CreditWorthinessFactor creditWorthinessFactor, Integer position) {
        AddDebtIncomeBottomSheet addDebtIncomeBottomSheet = new AddDebtIncomeBottomSheet();
        addDebtIncomeBottomSheet.setCreditWorthinessSource(creditWorthinessSource);
        switch (creditWorthinessSource) {
            case DEBT:
                addDebtIncomeBottomSheet.setDebtListener(this);
                break;
            case INCOME:
                addDebtIncomeBottomSheet.setIncomeListener(this);
                break;
            case EDIT_DEBT:
                addDebtIncomeBottomSheet.setCreditWorthinessFactor(creditWorthinessFactor);
                addDebtIncomeBottomSheet.setPosition(position);
                addDebtIncomeBottomSheet.setDebtListener(this);
                break;
            case EDIT_INCOME:
                addDebtIncomeBottomSheet.setCreditWorthinessFactor(creditWorthinessFactor);
                addDebtIncomeBottomSheet.setPosition(position);
                addDebtIncomeBottomSheet.setIncomeListener(this);
                break;
        }
        addDebtIncomeBottomSheet.show(getChildFragmentManager(), getString(R.string.debt_income));
    }

    public void showTotalDebts() {
        totalDebts = 0.0;
        for (CreditWorthinessFactor creditWorthinessFactor : debtCreditWorthinessFactors) {
            totalDebts = creditWorthinessFactor.getAmount() + totalDebts;
        }
        tvTotalDebt.setText(getString(R.string.total_debt, setPrecision(totalDebts)));
    }

    public void showTotalIncome() {
        totalIncome = 0.0;
        for (CreditWorthinessFactor creditWorthinessFactor : incomeCreditWorthinessFactors) {
            totalIncome = creditWorthinessFactor.getAmount() + totalIncome;
        }
        tvTotalIncome.setText(getString(R.string.total_income, setPrecision(totalIncome)));
    }

    public void showRatio() {
        if (!(totalIncome == 0)) {
            ratio = (totalDebts / totalIncome);
            tvDebtIncomeRatio.setText(getString(R.string.ratio, setPrecision(ratio)));
        } else {
            tvDebtIncomeRatio.setText(getString(R.string.ratio, setPrecision(0.0)));
        }
    }

    public void updateDebtsAndRatio() {
        showTotalDebts();
        showRatio();
    }

    public void updateIncomeAndRatio() {
        showTotalIncome();
        showRatio();
    }

    public String setPrecision(Double aDouble) {
        return String.format(Locale.ENGLISH, "%.2f", aDouble);
    }
}
