package com.mifos.apache.fineract.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

import com.mifos.apache.fineract.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Rajan Maurya
 *         On 22/07/17.
 */
public class ValidationUtil {

    private static final String NAME_REGEX_PATTERN = "^[\\p{L} .'-]+$";

    /**
     * Validates the Name
     *
     * @param string Name
     * @return Boolean
     */
    public static boolean isNameValid(String string) {
        return string.matches(NAME_REGEX_PATTERN);
    }

    private static String encode(String identifier) throws UnsupportedEncodingException {
        return URLEncoder.encode(identifier, "UTF-8");
    }

    public static Boolean isUrlSafe(String string) {
        try {
            encode(string);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Context context, String string, TextInputLayout inputLayout) {
        if (TextUtils.isEmpty(string)) {
            ValidateIdentifierUtil.showTextInputLayoutError(inputLayout,
                    context.getString(R.string.required));
            return false;
        }
        hideTextInputLayoutError(inputLayout);
        return true;
    }

    public static void showTextInputLayoutError(TextInputLayout textInputLayout,
            String errorMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorMessage);
    }

    public static void hideTextInputLayoutError(TextInputLayout textInputLayout) {
        textInputLayout.setErrorEnabled(false);
        textInputLayout.setError(null);
    }
}
