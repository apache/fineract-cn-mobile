package org.apache.fineract.ui.online.identification.identificationdetails;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.data.models.customer.identification.ScanCard;
import org.apache.fineract.ui.adapters.IdentificationScanAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.online.identification.createidentification.Action;
import org.apache.fineract.ui.online.identification.createidentification.identificationactivity.CreateIdentificationActivity;
import org.apache.fineract.ui.online.identification.uploadidentificationscan.AddScanIdentificationListener;
import org.apache.fineract.ui.online.identification.uploadidentificationscan.UploadIdentificationCardBottomSheet;
import org.apache.fineract.ui.online.identification.viewscancard.ViewScanCardActivity;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.DateUtils;
import org.apache.fineract.utils.MaterialDialog;
import org.apache.fineract.utils.Utils;

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
public class IdentificationDetailsFragment extends FineractBaseFragment
        implements IdentificationDetailsContract.View,
        IdentificationScanAdapter.OnItemClickListener, AddScanIdentificationListener {

    private static final Integer ACTION_EDIT_IDENTIFICATION_CARD = 1;

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

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    IdentificationScanAdapter identificationScanAdapter;

    @Inject
    IdentificationDetailsPresenter identificationDetailsPresenter;

    View rootView;

    private String customerIdentifier;
    private Identification identificationCard;
    private List<ScanCard> scanCards;

    public static IdentificationDetailsFragment newInstance(String customerIdentifier,
            Identification identification) {
        IdentificationDetailsFragment fragment = new IdentificationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_identification_details, container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
        identificationDetailsPresenter.attachView(this);

        showUserInterface();
        initializeRecyclerView();

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

    @OnClick(R.id.btn_try_again)
    void onRetry() {
        fetchIdentificationScanCard();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchIdentificationScanCard();
    }

    @Override
    public void updateScanUploadedIdentification() {
        fetchIdentificationScanCard();
    }

    @Override
    public void fetchIdentificationScanCard() {
        tvScansStatus.setVisibility(View.VISIBLE);
        rvScansUploaded.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
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
    }

    @Override
    public void initializeRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScansUploaded.setLayoutManager(layoutManager);
        rvScansUploaded.setHasFixedSize(true);
        identificationScanAdapter.setOnItemClickListener(this);
        rvScansUploaded.setAdapter(identificationScanAdapter);
    }

    @Override
    public void showScanCards(List<ScanCard> scanCards) {
        this.scanCards = scanCards;
        showRecyclerView(true);
        identificationScanAdapter.setScanCards(scanCards);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvScansUploaded.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvScansUploaded.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        }
        tvScansStatus.setVisibility(View.GONE);
    }

    @Override
    public void showScansStatus(String message) {
        showRecyclerView(false);
        showFineractEmptyUI(getString(R.string.identification_card_scans),
                getString(R.string.identification_card_scan), R.drawable.ic_description_black_24dp);
    }

    @Override
    public void showIdentifierDeletedSuccessfully() {
        Toast.makeText(getActivity(), R.string.identification_deleted_successfully,
                Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showIdentificationCardScanDeletedSuccessfully(ScanCard scanCard) {
        scanCards.remove(scanCard);
        identificationScanAdapter.notifyDataSetChanged();
        if (scanCards.size() == 0) {
            showScansStatus(getString(R.string.empty_scans_to_show));
        }
        Toast.makeText(getActivity(), R.string.identification_card_scan_deleted_successfully,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showProgressDialog() {
        showMifosProgressDialog(getString(R.string.deleting_identification_card));
    }

    @Override
    public void hideProgressDialog() {
        hideMifosProgressDialog();
    }

    @Override
    public void showNoInternetConnection() {
        showRecyclerView(false);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        showRecyclerView(false);
        showFineractErrorUI(getString(R.string.identification_card_scans));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), ViewScanCardActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        intent.putExtra(ConstantKeys.IDENTIFICATION_NUMBER, identificationCard.getNumber());
        intent.putExtra(ConstantKeys.IDENTIFICATION_SCAN_CARD, (new Gson()).toJson(scanCards));
        intent.putExtra(ConstantKeys.POSITION, position);
        startActivity(intent);
    }

    @Override
    public void onItemDelete(View view, final int position) {
        new MaterialDialog.Builder()
                .init(getActivity())
                .setTitle(getString(R.string.dialog_title_confirm_deletion))
                .setMessage(getString(
                        R.string.dialog_message_confirmation_delete_identification_card_scan))
                .setPositiveButton(getString(R.string.dialog_action_delete),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                identificationDetailsPresenter.deleteIdentificationCardScan(
                                        customerIdentifier, identificationCard.getNumber(),
                                        scanCards.get(position));
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_action_cancel))
                .createMaterialDialog()
                .show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_identification_card, menu);
        Utils.setToolbarIconColor(getActivity(), menu, R.color.white);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_identification_edit:
                Intent intent = new Intent(getActivity(), CreateIdentificationActivity.class);
                intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
                intent.putExtra(ConstantKeys.IDENTIFICATION_ACTION, Action.EDIT);
                intent.putExtra(ConstantKeys.IDENTIFICATION_CARD, identificationCard);
                startActivityForResult(intent, ACTION_EDIT_IDENTIFICATION_CARD);
                return true;
            case R.id.menu_identification_delete:
                new MaterialDialog.Builder()
                        .init(getActivity())
                        .setTitle(getString(R.string.dialog_title_confirm_deletion))
                        .setMessage(getString(
                                R.string.dialog_message_confirmation_delete_identification_card))
                        .setPositiveButton(getString(R.string.dialog_action_delete),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        identificationDetailsPresenter.deleteIdentificationCard(
                                                customerIdentifier, identificationCard.getNumber());
                                    }
                                })
                        .setNegativeButton(getString(R.string.dialog_action_cancel))
                        .createMaterialDialog()
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_EDIT_IDENTIFICATION_CARD && resultCode == Activity.RESULT_OK) {
            identificationCard = data.getParcelableExtra(ConstantKeys.IDENTIFICATION_CARD);
            showUserInterface();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressDialog();
        identificationDetailsPresenter.detachView();
    }
}
