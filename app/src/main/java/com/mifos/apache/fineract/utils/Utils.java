package com.mifos.apache.fineract.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Locale;

/**
 * @author Rajan Maurya
 *         On 30/07/17.
 */

public class Utils {

    public static <T> T getStringToPoJo(TypeToken<T> listModel, String jsonName) {
        return new Gson().fromJson(jsonName, listModel.getType());
    }

    public static String getPrecision(Double aDouble) {
        return String.format(Locale.ENGLISH, "%.2f", aDouble);
    }
}
