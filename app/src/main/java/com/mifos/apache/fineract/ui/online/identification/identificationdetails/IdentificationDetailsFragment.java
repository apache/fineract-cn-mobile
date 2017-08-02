package com.mifos.apache.fineract.ui.online.identification.identificationdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.identification.Identification;
import com.mifos.apache.fineract.data.models.customer.identification.ScanCard;
import com.mifos.apache.fineract.ui.adapters.IdentificationScanAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.online.identification.uploadidentificationscan
        .AddScanIdentificationListener;
import com.mifos.apache.fineract.ui.online.identification.uploadidentificationscan
        .UploadIdentificationCardBottomSheet;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.mifos.apache.fineract.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */
public class IdentificationDetailsFragment extends MifosBaseFragment
        implements IdentificationDetailsContract.View,
        IdentificationScanAdapter.OnItemClickListener, AddScanIdentificationListener {

    @BindView(R.id.tv_number)
    TextView tvNumber;

    @BindView(R.id.tv_type)
    TextView tvType;

    @BindView(R.id.tv_expiration_date)
    TextView tvExpirationDate;

    @BindView(R.id.tv_issuer)
    TextView tvIssuer;

    @BindView(R.id.tv_scans_status)
    TextView tvScansStatus;

    @BindView(R.id.rv_scans_uploaded)
    RecyclerView rvScansUploaded;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.tv_error)
    TextView tvError;

    @Inject
    IdentificationScanAdapter identificationScanAdapter;

    @Inject
    IdentificationDetailsPresenter identificationDetailsPresenter;

    View rootView;

    private String customerIdentifier;
    private Identification identificationCard;

    public static IdentificationDetailsFragment newInstance(String identifier,
            Identification identification) {
        IdentificationDetailsFragment fragment = new IdentificationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, identifier);
        args.putParcelable(ConstantKeys.IDENTIFICATION_CARD, identification);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
            identificationCard = getArguments().getParcelable(ConstantKeys.IDENTIFICATION_CARD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_identification_details, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        identificationDetailsPresenter.attachView(this);

        showUserInterface();

        return rootView;
    }

    @OnClick(R.id.fab_upload_identification_scan_card)
    void addIdentificationCard() {
        UploadIdentificationCardBottomSheet uploadIdentificationCardBottomSheet =
                new UploadIdentificationCardBottomSheet();
        uploadIdentificationCardBottomSheet.setIdentifierAndNumber(customerIdentifier,
                identificationCard.getNumber());
        uploadIdentificationCardBottomSheet.setAddScanIdentificationListener(this);
        uploadIdentificationCardBottomSheet.show(getChildFragmentManager(),
                getString(R.string.upload_new_identification_card_scan));
    }

    @OnClick(R.id.iv_retry)
    void onRetry() {
        identificationDetailsPresenter.fetchIdentificationScanCards(customerIdentifier,
                identificationCard.getNumber());
    }

    @Override
    public void onResume() {
        super.onResume();
        identificationDetailsPresenter.fetchIdentificationScanCards(customerIdentifier,
                identificationCard.getNumber());
    }

    @Override
    public void updateScanUploadedIdentification() {
        tvScansStatus.setVisibility(View.VISIBLE);
        rvScansUploaded.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
        identificationDetailsPresenter.fetchIdentificationScanCards(customerIdentifier,
                identificationCard.getNumber());
    }

    @Override
    public void showUserInterface() {
        tvNumber.setText(identificationCard.getNumber());
        tvIssuer.setText(identificationCard.getIssuer());
        tvType.setText(identificationCard.getType());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, identificationCard.getExpirationDate().getYear());
        calendar.set(Calendar.MONTH, identificationCard.getExpirationDate().getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, identificationCard.getExpirationDate().getDay());
        tvExpirationDate.setText(DateUtils.convertServerDate(calendar));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScansUploaded.setLayoutManager(layoutManager);
        rvScansUploaded.setHasFixedSize(true);
        identificationScanAdapter.setOnItemClickListener(this);
        rvScansUploaded.setAdapter(identificationScanAdapter);
    }

    @Override
    public void showScanCards(List<ScanCard> scanCards) {
        showRecyclerView(true);
        identificationScanAdapter.setScanCards(scanCards);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvScansUploaded.setVisibility(View.VISIBLE);
            rlError.setVisibility(View.GONE);
        } else {
            rvScansUploaded.setVisibility(View.GONE);
            rlError.setVisibility(View.VISIBLE);
        }
        tvScansStatus.setVisibility(View.GONE);
    }

    @Override
    public void showScansStatus(String message) {
        showRecyclerView(false);
        rlError.getChildAt(0).setVisibility(View.GONE);
        tvError.setText(message);
    }

    @Override
    public void showError(String message) {
        showRecyclerView(false);
        tvError.setText(message);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemDelete(View view, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        identificationDetailsPresenter.detachView();
    }
}
