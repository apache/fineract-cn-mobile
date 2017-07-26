package com.mifos.apache.fineract.utils;

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
}
