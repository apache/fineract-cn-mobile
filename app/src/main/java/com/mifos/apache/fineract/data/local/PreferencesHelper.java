package com.mifos.apache.fineract.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.mifos.apache.fineract.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    public static final String PREF_MIFOS = "preferences_mifos";

    private final SharedPreferences preferences;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        preferences = context.getSharedPreferences(PREF_MIFOS, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void clear() {
        getPreferences().edit().clear().apply();
    }

    public int getInt(String preferenceKey, int preferenceDefaultValue) {
        return getPreferences().getInt(preferenceKey, preferenceDefaultValue);
    }

    public void putInt(String preferenceKey, int preferenceValue) {
        getPreferences().edit().putInt(preferenceKey, preferenceValue).apply();
    }

    public long getLong(String preferenceKey, long preferenceDefaultValue) {
        return getPreferences().getLong(preferenceKey, preferenceDefaultValue);
    }

    public void putLong(String preferenceKey, long preferenceValue) {
        getPreferences().edit().putLong(preferenceKey, preferenceValue).apply();
    }

    public float getFloat(String preferenceKey, float preferenceDefaultValue) {
        return getPreferences().getFloat(preferenceKey, preferenceDefaultValue);
    }

    public void putFloat(String preferenceKey, float preferenceValue) {
        getPreferences().edit().putFloat(preferenceKey, preferenceValue).apply();
    }

    public boolean getBoolean(String preferenceKey, boolean preferenceDefaultValue) {
        return getPreferences().getBoolean(preferenceKey, preferenceDefaultValue);
    }

    public void putBoolean(String preferenceKey, boolean preferenceValue) {
        getPreferences().edit().putBoolean(preferenceKey, preferenceValue).apply();
    }

    public String getString(String preferenceKey, String preferenceDefaultValue) {
        return getPreferences().getString(preferenceKey, preferenceDefaultValue);
    }

    public void putString(String preferenceKey, String preferenceValue) {
        getPreferences().edit().putString(preferenceKey, preferenceValue).apply();
    }
}
