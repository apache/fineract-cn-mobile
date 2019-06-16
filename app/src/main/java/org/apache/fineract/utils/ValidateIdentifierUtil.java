package org.apache.fineract.utils;

import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;

import org.apache.fineract.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Rajan Maurya
 *         On 02/08/17.
 */
public class ValidateIdentifierUtil {

    public static boolean isValid(Context context, String string, TextInputLayout textInputLayout) {
        if (TextUtils.isEmpty(string)) {
            showTextInputLayoutError(textInputLayout, context.getString(R.string.unique_required));
            return false;
        }
        return validate(context, string, textInputLayout);
    }

    private static boolean validate(Context context, String string,
            TextInputLayout textInputLayout) {
        if (string.length() < 3) {
            showTextInputLayoutError(textInputLayout,
                    context.getString(R.string.must_be_at_least_n_characters, 3));
            return false;
        }

        if (string.length() > 32) {
            showTextInputLayoutError(textInputLayout,
                    context.getString(R.string.only_thirty_two_character_allowed));
            return false;
        }

        try {
            if (encode(string).equals(string)) {
                showTextInputLayoutError(textInputLayout, null);
                return true;
            } else {
                showTextInputLayoutError(textInputLayout, context.getString(
                        R.string.only_alphabetic_decimal_digits_characters_allowed));
                return false; //If we can't encode with UTF-8, then there are no valid names.
            }
        } catch (UnsupportedEncodingException e) {
            showTextInputLayoutError(textInputLayout,
                    context.getString(R.string.only_alphabetic_decimal_digits_characters_allowed));
            return false; //If we can't encode with UTF-8, then there are no valid names.
        }
    }

    private static String encode(String identifier) throws UnsupportedEncodingException {
        return URLEncoder.encode(identifier, "UTF-8");
    }

    public static void showTextInputLayoutError(TextInputLayout textInputLayout,
            String errorMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorMessage);
    }
}
