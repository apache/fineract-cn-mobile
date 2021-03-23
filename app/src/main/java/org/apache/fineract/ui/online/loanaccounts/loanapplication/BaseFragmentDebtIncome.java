package org.apache.fineract.ui.online.loanaccounts.loanapplication;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.CreditWorthinessFactor;
import org.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import org.apache.fineract.ui.adapters.LoanDebtIncomeAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.utils.MaterialDialog;

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
public abstract class BaseFragmentDebtIncome extends FineractBaseFragment implements
        OnBottomSheetDialogListener.AddDebt, OnBottomSheetDialogListener.AddIncome,
        LoanDebtIncomeAdapter.OnClickEditDeleteListener {

    public static final String LOG_TAG = BaseFragmentDebtIncome.class.getSimpleName();

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

    View rootView;

    private Double totalDebts = 0.0;
    private Double totalIncome = 0.0;
    private Double ratio = 00.00;

    private List<CreditWorthinessFactor> debtCreditWorthinessFactors;
    private List<CreditWorthinessFactor> incomeCreditWorthinessFactors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debtCreditWorthinessFactors = new ArrayList<>();
        incomeCreditWorthinessFactors = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getFragmentLayout(), container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);

        showUserInterface();

        return rootView;
    }

    /**
     * Every fragment has to inflate a layout in the onCreateView method. We have added this method
     * to
     * avoid duplicate all the inflate code in every fragment. You only have to return the layout
     * to
     * inflate in this method when extends BaseFragment.
     */
    protected abstract int getFragmentLayout();

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

    public CreditWorthinessSnapshot getCreditWorthinessSnapshot() {
        CreditWorthinessSnapshot creditWorthinessSnapshot = new CreditWorthinessSnapshot();
        creditWorthinessSnapshot.setIncomeSources(incomeCreditWorthinessFactors);
        creditWorthinessSnapshot.setDebts(debtCreditWorthinessFactors);
        return creditWorthinessSnapshot;
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
        String msg = "";
        switch (creditWorthinessSource) {
            case DEBT:
                msg = debtCreditWorthinessFactors.get(position).getDescription();
                break;
            case INCOME:
                msg = incomeCreditWorthinessFactors.get(position).getDescription();
                break;
        }
        new MaterialDialog.Builder()
                .init(getContext())
                .setTitle(getString(R.string.dialog_title_confirm_deletion))
                .setMessage(getString(R.string.dialog_message_confirm_name_deletion, msg))
                .setPositiveButton(getString(R.string.delete),
                        (dialog, which) -> {
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
                        })
                .setNegativeButton(getString(R.string.dialog_action_cancel))
                .createMaterialDialog()
                .show();
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
