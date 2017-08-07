package com.mifos.apache.fineract.utils;

public class AndroidVersionUtil {

    public static int getApiVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static boolean isApiVersionGreaterOrEqual(int thisVersion) {
        return android.os.Build.VERSION.SDK_INT >= thisVersion;
    }
}