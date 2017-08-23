package com.mifos.apache.fineract.ui.online.loanaccounts.debtincomereport;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.loan.CreditWorthinessFactor;
import com.mifos.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import com.mifos.apache.fineract.ui.adapters.ViewPagerAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.mifos.apache.fineract.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 30/07/17.
 */
public class DebtIncomeReportActivity extends MifosBaseActivity {

    @BindView(R.id.tl_debt_income)
    TabLayout tlDebtIncome;

    @BindView(R.id.vp_debt_income)
    ViewPager vpDebtIncome;

    private List<CreditWorthinessSnapshot> creditWorthinessSnapshots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_income_report);
        ButterKnife.bind(this);

        String debtIncomeString = getIntent().getStringExtra(
                ConstantKeys.LOAN_CREDITWORTHINESSSNAPSHOTS);
        creditWorthinessSnapshots = Utils.getStringToPoJo(
                new TypeToken<List<CreditWorthinessSnapshot>>() {
                }, debtIncomeString);

        showBackButton();
        setToolbarTitle(getString(R.string.view_debt_income_ratio));

        setupViewPager(vpDebtIncome);
        tlDebtIncome.setupWithViewPager(vpDebtIncome);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        switch (creditWorthinessSnapshots.size()) {
            case 0:
                adapter.addFragment(
                        DebtIncomeReportFragment.newInstance(new CreditWorthinessSnapshot()),
                        getString(R.string.customer_ratio, "0.0"));
                adapter.addFragment(
                        DebtIncomeReportFragment.newInstance(new CreditWorthinessSnapshot()),
                        getString(R.string.co_signer_ratio, "0.0"));
                break;
            case 1:
                adapter.addFragment(
                        DebtIncomeReportFragment.newInstance(creditWorthinessSnapshots.get(0)),
                        getString(R.string.customer_ratio,
                                getRatio(creditWorthinessSnapshots.get(0))));
                adapter.addFragment(
                        DebtIncomeReportFragment.newInstance(new CreditWorthinessSnapshot()),
                        getString(R.string.co_signer_ratio, "0.0"));
                break;
            case 2:
                adapter.addFragment(
                        DebtIncomeReportFragment.newInstance(creditWorthinessSnapshots.get(0)),
                        getString(R.string.customer_ratio,
                                getRatio(creditWorthinessSnapshots.get(0))));
                adapter.addFragment(
                        DebtIncomeReportFragment.newInstance(creditWorthinessSnapshots.get(1)),
                        getString(R.string.co_signer_ratio,
                                getRatio(creditWorthinessSnapshots.get(1))));
                break;
        }
        viewPager.setAdapter(adapter);
    }

    private String getRatio(CreditWorthinessSnapshot creditWorthinessSnapshot) {
        Double amountDebt = 0.0;
        Double amountIncome = 0.0;
        for (CreditWorthinessFactor factor : creditWorthinessSnapshot.getIncomeSources()) {
            amountIncome = amountIncome + factor.getAmount();
        }
        for (CreditWorthinessFactor factor : creditWorthinessSnapshot.getDebts()) {
            amountDebt = amountDebt + factor.getAmount();
        }
        if (!(amountIncome == 0)) {
            return Utils.getPrecision(amountDebt / amountIncome);
        }
        return Utils.getPrecision(0.0);
    }
}
