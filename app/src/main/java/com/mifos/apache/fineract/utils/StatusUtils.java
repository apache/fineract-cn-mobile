package com.mifos.apache.fineract.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Customer;

/**
 * @author Rajan Maurya
 *         On 05/08/17.
 */
public class StatusUtils {

    public static void setCustomerStatus (Customer.State state, AppCompatImageView imageView,
            Context context) {
        switch (state) {
            case ACTIVE:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.deposit_green));
                break;
            case CLOSED:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.black));
                break;
            case LOCKED:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case PENDING:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.light_yellow));
                break;
        }
    }
}

