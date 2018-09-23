package org.apache.fineract.ui.online.identification.createidentification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.ui.base.FineractBaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class FormOverViewIdentificationFragment extends FineractBaseFragment implements Step,
        OverViewContract {

    public static final String DATE_FORMAT = "dd MMM yyyy";

    @BindView(R.id.tv_number)
    TextView tvNumber;

    @BindView(R.id.tv_type)
    TextView tvType;

    @BindView(R.id.tv_expiration_date)
    TextView tvExpirationDate;

    @BindView(R.id.tv_issuer)
    TextView tvIssuer;

    View rootView;

    public static FormOverViewIdentificationFragment newInstance() {
        FormOverViewIdentificationFragment fragment = new FormOverViewIdentificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_form_overview_identification,
                container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


    @Override
    public void setIdentification(Identification identification) {

        tvIssuer.setText(identification.getIssuer());
        tvNumber.setText(identification.getNumber());
        tvType.setText(identification.getType());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, identification.getExpirationDate().getYear());
        calendar.set(Calendar.MONTH, identification.getExpirationDate().getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, identification.getExpirationDate().getDay());

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        tvExpirationDate.setText(sdf.format(calendar.getTime()));
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
}
