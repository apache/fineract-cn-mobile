package com.mifos.apache.fineract.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.data.models.deposit.DepositAccount;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;

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

    public static void setCustomerStatusIcon(Customer.State state, ImageView imageView,
            Context context) {
        switch (state) {
            case ACTIVE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_check_circle_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case CLOSED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_close_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case LOCKED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_lock_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case PENDING:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_hourglass_empty_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
        }
    }

    public static void setLoanAccountStatus (LoanAccount.State state, AppCompatImageView imageView,
            Context context) {
        switch (state) {
            case CREATED:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
            case PENDING:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;

            case APPROVED:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.deposit_green));
                break;

            case ACTIVE:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.deposit_green));
                break;

            case CLOSED:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;

        }
    }

    public static void setLoanAccountStatusIcon(LoanAccount.State state, ImageView imageView,
            Context context) {
        switch (state) {
            case ACTIVE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_check_circle_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case CLOSED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_close_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case APPROVED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_done_all_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case PENDING:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_hourglass_empty_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
            case CREATED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_hourglass_empty_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
        }
    }

    public static void setDepositAccountStatusIcon(DepositAccount.State state, ImageView imageView,
            Context context) {
        switch (state) {
            case ACTIVE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_check_circle_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case CLOSED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_close_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case APPROVED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_done_all_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case PENDING:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_hourglass_empty_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
            case CREATED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_hourglass_empty_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
            case LOCKED:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_lock_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
        }
    }
}

