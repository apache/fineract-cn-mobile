package org.apache.fineract.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    public static void setToolbarIconColor(Context context, Menu menu, int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(
                        ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    public static void hideSoftKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
