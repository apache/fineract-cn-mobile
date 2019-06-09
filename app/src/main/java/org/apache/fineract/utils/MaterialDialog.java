package org.apache.fineract.utils;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import org.apache.fineract.R;

/**
 * This Class is the Material Dialog Builder Class
 * Created by Rajan Maurya on 03/08/16.
 */
public final class MaterialDialog {

    public static class Builder {

        private AlertDialog.Builder materialDialogBuilder;

        //This is the Default Builder Initialization with Material Style
        public Builder init(Context context) {
            materialDialogBuilder =
                    new AlertDialog.Builder(context, R.style.MaterialAlertDialogStyle);
            return this;
        }

        //This method set the custom Material Style
        public Builder init(Context context, int theme) {
            materialDialogBuilder = new AlertDialog.Builder(context, theme);
            return this;
        }

        //This method set the String Title
        public Builder setTitle(String title) {
            materialDialogBuilder.setTitle(title);
            return this;
        }

        //This Method set the String Resources to Title
        public Builder setTitle(@StringRes int title) {
            materialDialogBuilder.setTitle(title);
            return this;
        }

        //This Method set the String Message
        public Builder setMessage(String message) {
            materialDialogBuilder.setMessage(message);
            return this;
        }

        //This Method set the String Resources message
        public Builder setMessage(@StringRes int message) {
            materialDialogBuilder.setMessage(message);
            return this;
        }

        //This Method set String Test to the Positive Button and set the Onclick null
        public Builder setPositiveButton(String positiveText) {
            materialDialogBuilder.setPositiveButton(positiveText, null);
            return this;
        }

        //This Method Set the String Resources Text To Positive Button
        public Builder setPositiveButton(@StringRes int positiveText) {
            materialDialogBuilder.setPositiveButton(positiveText, null);
            return this;
        }

        //This Method set the String Text to Positive Button and set the OnClick Event to it
        public Builder setPositiveButton(String positiveText,
                DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setPositiveButton(positiveText, listener);
            return this;
        }

        //This method set the String Resources text To Positive button and set the Onclick Event
        public Builder setPositiveButton(@StringRes int positiveText,
                DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setPositiveButton(positiveText, listener);
            return this;
        }

        //This Method the String Text to Negative Button and Set the onclick event to null
        public Builder setNegativeButton(String negativeText) {
            materialDialogBuilder.setNegativeButton(negativeText, null);
            return this;
        }

        //This Method set the String Resources Text to Negative button
        // and set the onclick event to null
        public Builder setNegativeButton(@StringRes int negativeText) {
            materialDialogBuilder.setNegativeButton(negativeText, null);
            return this;
        }

        //This Method set String Text to Negative Button and
        //Set the Onclick event
        public Builder setNegativeButton(String negativeText,
                DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setNegativeButton(negativeText, listener);
            return this;
        }

        //This method set String Resources Text to Negative Button and set Onclick Event
        public Builder setNegativeButton(@StringRes int negativeText,
                DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setNegativeButton(negativeText, listener);
            return this;
        }

        //This Method the String Text to Neutral Button and Set the onclick event to null
        public Builder setNeutralButton(String neutralText) {
            materialDialogBuilder.setNeutralButton(neutralText, null);
            return this;
        }

        //This Method set the String Resources Text to Neutral button
        // and set the onclick event to null
        public Builder setNeutralButton(@StringRes int neutralText) {
            materialDialogBuilder.setNeutralButton(neutralText, null);
            return this;
        }

        //This Method set String Text to Neutral Button and
        //Set the Onclick event
        public Builder setNeutralButton(String neutralText,
                DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setNeutralButton(neutralText, listener);
            return this;
        }

        //This method set String Resources Text to Neutral Button and set Onclick Event
        public Builder setNeutralButton(@StringRes int neutralText,
                DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setNeutralButton(neutralText, listener);
            return this;
        }

        public Builder setCancelable(Boolean cancelable) {
            materialDialogBuilder.setCancelable(cancelable);
            return this;
        }

        public Builder setItems(int items, DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setItems(items, listener);
            return this;
        }

        public Builder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            materialDialogBuilder.setItems(items, listener);
            return this;
        }

        //This Method Create the Final Material Dialog
        public Builder createMaterialDialog() {
            materialDialogBuilder.create();
            return this;
        }

        //This Method Show the Dialog
        public Builder show() {
            materialDialogBuilder.show();
            return this;
        }
    }
}
