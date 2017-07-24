package com.mifos.apache.fineract.utils;

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
}
