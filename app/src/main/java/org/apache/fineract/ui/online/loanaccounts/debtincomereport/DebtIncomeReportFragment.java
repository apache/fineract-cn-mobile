package org.apache.fineract.ui.online.loanaccounts.debtincomereport;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.CreditWorthinessFactor;
import org.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import org.apache.fineract.ui.adapters.DebtIncomeReportAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 30/07/17.
 */
public class DebtIncomeReportFragment extends FineractBaseFragment {

    @BindView(R.id.rv_debt)
    RecyclerView rvDebt;

    @BindView(R.id.rv_income)
    RecyclerView rvIncome;

    @BindView(R.id.tv_income_ratio)
    TextView tvIncomeRatio;

    @BindView(R.id.tv_debt_ratio)
    TextView tvDebtRatio;

    @BindView(R.id.rl_is_empty_debt)
    RelativeLayout rlIsEmptyDebt;

    @BindView(R.id.rl_is_empty_income)
    RelativeLayout rlIsEmptyIncome;

    @BindView(R.id.ll_debt)
    LinearLayout llDebt;

    @BindView(R.id.ll_income)
    LinearLayout llIncome;

    View rootView;

    @Inject
    DebtIncomeReportAdapter debtAdapter;

    @Inject
    DebtIncomeReportAdapter incomeAdapter;

    private CreditWorthinessSnapshot creditWorthinessSnapshot;

    public static DebtIncomeReportFragment newInstance(CreditWorthinessSnapshot snapshot) {
        DebtIncomeReportFragment fragment = new DebtIncomeReportFragment();
        Bundle args = new Bundle();
        args.putParcelable(ConstantKeys.LOAN_CREDITWORTHINESSSNAPSHOTS, snapshot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            creditWorthinessSnapshot = getArguments().getParcelable(
                    ConstantKeys.LOAN_CREDITWORTHINESSSNAPSHOTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_debt_income_report, container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);

        showUserInterface();

        return rootView;
    }

    public void showUserInterface() {
        LinearLayoutManager debtLayoutManager = new LinearLayoutManager(getActivity());
        debtLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDebt.setLayoutManager(debtLayoutManager);
        rvDebt.setHasFixedSize(true);
        debtAdapter.setCreditWorthinessFactors(creditWorthinessSnapshot.getDebts());
        rvDebt.setAdapter(debtAdapter);

        LinearLayoutManager incomeLayoutManager = new LinearLayoutManager(getActivity());
        incomeLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvIncome.setLayoutManager(incomeLayoutManager);
        rvIncome.setHasFixedSize(true);
        incomeAdapter.setCreditWorthinessFactors(creditWorthinessSnapshot.getIncomeSources());
        rvIncome.setAdapter(incomeAdapter);

        tvDebtRatio.setText(getString(R.string.total_debt,
                getTotalAmount(creditWorthinessSnapshot.getDebts())));
        tvIncomeRatio.setText(getString(R.string.total_income,
                getTotalAmount(creditWorthinessSnapshot.getIncomeSources())));

        if (creditWorthinessSnapshot.getDebts().size() == 0) {
            rlIsEmptyDebt.setVisibility(View.VISIBLE);
            llDebt.setVisibility(View.GONE);
            rlIsEmptyDebt.getChildAt(0).setVisibility(View.GONE);
            TextView message = (TextView) rlIsEmptyDebt.getChildAt(1);
            message.setText(getString(R.string.empty_debts_to_show));
        }

        if (creditWorthinessSnapshot.getIncomeSources().size() == 0) {
            rlIsEmptyIncome.setVisibility(View.VISIBLE);
            llIncome.setVisibility(View.GONE);
            rlIsEmptyIncome.getChildAt(0).setVisibility(View.GONE);
            TextView message = (TextView) rlIsEmptyIncome.getChildAt(1);
            message.setText(getString(R.string.empty_income_to_show));
        }
    }

    public String getTotalAmount(List<CreditWorthinessFactor> creditWorthinessFactors) {
        Double amount = 0.0;
        for (CreditWorthinessFactor factor : creditWorthinessFactors) {
            amount = amount + factor.getAmount();
        }
        return Utils.getPrecision(amount);
    }
}
