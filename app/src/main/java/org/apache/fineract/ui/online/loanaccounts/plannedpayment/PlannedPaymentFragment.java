package org.apache.fineract.ui.online.loanaccounts.plannedpayment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.payment.PlannedPayment;
import org.apache.fineract.ui.adapters.PlannedPaymentAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.DateUtils;
import org.zakariya.stickyheaders.PagedLoadScrollListener;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */
public class PlannedPaymentFragment extends FineractBaseFragment
        implements PlannedPaymentContract.View, SwipeRefreshLayout.OnRefreshListener,
        CalendarView.OnDateChangeListener {

    public static final String LOG_TAG = PlannedPaymentFragment.class.getSimpleName();

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_planned_payment)
    RecyclerView rvPlannedPayment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cv_planned_payment)
    CardView cvCalenderPlannedPayment;

    @BindView(R.id.layout_error)
    View layoutError;

    @BindView(R.id.tv_toolbar_date)
    TextView tvToolbarDate;

    @BindView(R.id.calender_view_payment)
    CalendarView calenderViewPayment;

    @Inject
    PlannedPaymentAdapter plannedPaymentAdapter;

    @Inject
    PlannedPaymentPresenter plannedPaymentPresenter;

    View rootView;

    private String productIdentifier;
    private String caseIdentifier;
    private boolean isCalenderVisible;
    private String initialDisbursalDate = null;

    public static PlannedPaymentFragment newInstance(String productIdentifier,
            String caseIdentifier) {
        PlannedPaymentFragment fragment = new PlannedPaymentFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.PRODUCT_IDENTIFIER, productIdentifier);
        args.putString(ConstantKeys.CASE_IDENTIFIER, caseIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productIdentifier = getArguments().getString(ConstantKeys.PRODUCT_IDENTIFIER);
            caseIdentifier = getArguments().getString(ConstantKeys.CASE_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_planned_payment, container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
        plannedPaymentPresenter.attachView(this);

        showUserInterface();

        fetchPlannedPayment();

        return rootView;
    }

    @Override
    public void fetchPlannedPayment() {
        rvPlannedPayment.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        plannedPaymentPresenter.fetchPlannedPayment(productIdentifier, caseIdentifier, 0,
                initialDisbursalDate, false);
    }

    @Override
    public void showUserInterface() {
        //TODO fix null pointer exception in toolbar
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        }
        StickyHeaderLayoutManager layoutManager = new StickyHeaderLayoutManager();
        rvPlannedPayment.setLayoutManager(layoutManager);
        rvPlannedPayment.setHasFixedSize(true);
        rvPlannedPayment.setAdapter(plannedPaymentAdapter);
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);
        rvPlannedPayment.addOnScrollListener(new PagedLoadScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, LoadCompleteNotifier loadComplete) {
                plannedPaymentPresenter.fetchPlannedPayment(productIdentifier, caseIdentifier, page,
                        initialDisbursalDate, true);
            }
        });
        calenderViewPayment.setOnDateChangeListener(this);
        tvToolbarDate.setText(DateUtils.getCurrentDate());
    }

    @OnClick(R.id.ll_toolbar_date)
    void showCalender() {
        if (!isCalenderVisible) {
            cvCalenderPlannedPayment.setVisibility(View.VISIBLE);
            isCalenderVisible = true;
            tvToolbarDate.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_drop_up_black_24dp, 0);
        } else {
            cvCalenderPlannedPayment.setVisibility(View.GONE);
            tvToolbarDate.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
            isCalenderVisible = false;
        }
    }

    @OnClick(R.id.btn_try_again)
    void onRetry() {
        fetchPlannedPayment();
    }

    @Override
    public void onRefresh() {
        plannedPaymentPresenter.fetchPlannedPayment(productIdentifier, caseIdentifier, 0,
                initialDisbursalDate, false);
    }

    @OnClick(R.id.btn_load_planned_payment)
    void loadPlannedPaymentAccordingToDate() {
        isCalenderVisible = false;
        cvCalenderPlannedPayment.setVisibility(View.GONE);
        plannedPaymentPresenter.fetchPlannedPayment(productIdentifier, caseIdentifier, 0,
                initialDisbursalDate, false);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month,
            int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        tvToolbarDate.setText(DateUtils.getDate(DateUtils.getDateInUTC(calendar),
                DateUtils.STANDARD_DATE_TIME_FORMAT, DateUtils.OUTPUT_DATE_FORMAT));
        initialDisbursalDate = DateUtils.getDateInUTC(calendar);
    }


    @Override
    public void showPlannedPayment(List<PlannedPayment> plannedPayments) {
        showRecyclerView(true);
        plannedPaymentAdapter.setPlannedPayment(plannedPayments);
    }

    @Override
    public void showMorePlannedPayments(List<PlannedPayment> plannedPayments) {
        showRecyclerView(true);
        plannedPaymentAdapter.setMorePlannedPayment(plannedPayments);
    }

    @Override
    public void showEmptyPayments(String message) {
        showRecyclerView(false);
        showFineractEmptyUI(getString(R.string.planned_payments),
                getString(R.string.planned_payments), R.drawable.ic_payment_black_24dp);
    }

    @Override
    public void showRecyclerView(boolean visible) {
        if (visible) {
            rvPlannedPayment.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvPlannedPayment.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressbar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressbar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showNoInternetConnection() {
        showRecyclerView(false);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        if (plannedPaymentAdapter.getItemCount() != 0) {
            showMessage(message);
        } else {
            showRecyclerView(false);
            showFineractErrorUI(getString(R.string.planned_payments));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        plannedPaymentPresenter.detachView();
    }
}
