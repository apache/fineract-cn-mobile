package org.apache.fineract.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

import org.apache.fineract.R;
import org.apache.fineract.data.models.Group;
import org.apache.fineract.data.models.accounts.AccountType;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.teller.Teller;

/**
 * @author Rajan Maurya
 * On 05/08/17.
 */
public class StatusUtils {

    public static void setCustomerStatus(Customer.State state, AppCompatImageView imageView,
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

    public static void setTellerStatus(Teller.State state, AppCompatImageView imageView,
                                       Context context) {
        switch (state) {
            case ACTIVE:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.deposit_green));
                break;
            case CLOSED:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case OPEN:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
            case PAUSED:
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

    public static void setLoanAccountStatus(LoanAccount.State state, AppCompatImageView imageView,
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

    public static void setGroupsStatusIcon(Group.Status state, ImageView imageView,
                                           Context context) {
        switch (state) {

            case PENDING:
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.blue));
                break;
            case ACTIVE:
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.deposit_green));
                break;
            case CLOSED:
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.red_dark));
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

    public enum Action {

        @SerializedName("ACTIVATE")
        ACTIVATE,

        @SerializedName("LOCK")
        LOCK,

        @SerializedName("UNLOCK")
        UNLOCK,

        @SerializedName("CLOSE")
        CLOSE,

        @SerializedName("REOPEN")
        REOPEN
    }

    public static void setCustomerActivitiesStatusIcon(Command.Action action, ImageView imageView,
                                                       Context context) {
        switch (action) {
            case ACTIVATE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_check_circle_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case CLOSE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_close_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case LOCK:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_lock_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case UNLOCK:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_lock_open_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case REOPEN:
                imageView.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_lock_open_black_24dp));
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;

        }
    }

    public static void setAccountType(AccountType action, ImageView imageView,
                                      Context context) {
        switch (action) {
            case ASSET:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.status));
                break;
            case LIABILITY:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_dark));
                break;
            case EQUITY:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.blue));
                break;
            case REVENUE:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.green_dark));
                break;
            case EXPENSE:
                imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.red_light));
                break;

        }
    }
}

