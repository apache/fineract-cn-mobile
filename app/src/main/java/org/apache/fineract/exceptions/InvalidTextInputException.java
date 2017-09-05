package org.apache.fineract.exceptions;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.apache.fineract.ui.base.Toaster;

public class InvalidTextInputException extends Exception {
    public static final String TYPE_ALPHABETS = "Alphabets";
    private String fieldInput;
    private String localisedErrorMessage;
    private String inputType;

    public InvalidTextInputException(String fieldInput, String localisedErrorMessage, String
            inputType) {
        this.fieldInput = fieldInput;
        this.localisedErrorMessage = localisedErrorMessage;
        this.inputType = inputType;
    }

    @Override
    public String toString() {
        return fieldInput + " " + localisedErrorMessage + " " + inputType;
    }


    public void notifyUserWithToast(Context context) {
        Toast.makeText(context, toString(), Toast.LENGTH_SHORT).show();
    }

    public void notifyUserWithToaster(View view) {
        Toaster.show(view, toString(), Toast.LENGTH_SHORT);
    }
}

