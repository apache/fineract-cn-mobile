package org.apache.fineract.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.injection.ApplicationContext;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    private final SharedPreferences preferences;
    private final Gson gson;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        preferences = context.getSharedPreferences(PreferenceKey.PREF_MIFOS, Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                .create();
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

    public void putStringSet(String preferenceKey, Set<String> stringSet) {
        getPreferences().edit().putStringSet(preferenceKey, stringSet).apply();
    }

    public Set<String> getStringSet(String preferenceKey) {
        return getPreferences().getStringSet(preferenceKey, null);
    }

    public void putAccessToken(String accessToken) {
        getPreferences().edit().putString(PreferenceKey.PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Nullable
    public String getAccessToken() {
        return getPreferences().getString(PreferenceKey.PREF_KEY_ACCESS_TOKEN, null);
    }

    public void putTenantIdentifier(String tenantIdentifier) {
        getPreferences().edit().putString(PreferenceKey.PREF_KEY_TENANT_IDENTIFIER,
                tenantIdentifier).apply();
    }

    public String getTenantIdentifier() {
        return getPreferences().getString(PreferenceKey.PREF_KEY_TENANT_IDENTIFIER, null);
    }

    public void putSignInUser(Authentication user) {
        getPreferences().edit().putString(PreferenceKey.PREF_KEY_SIGNED_IN_USER,
                gson.toJson(user)).apply();
    }

    public Authentication getSignedInUser() {
        String userJson = getPreferences().getString(PreferenceKey.PREF_KEY_SIGNED_IN_USER, null);
        if (userJson == null) return null;
        return gson.fromJson(userJson, Authentication.class);
    }

    public void putUserName(String username) {
        getPreferences().edit().putString(PreferenceKey.PREF_KEY_USER_NAME, username).apply();
    }

    public String getUserName() {
        return getPreferences().getString(PreferenceKey.PREF_KEY_USER_NAME, null);
    }

    public void setFetching(boolean isFetching) {
        getPreferences().edit().putBoolean(PreferenceKey.PREF_IS_FETCHING, isFetching).apply();
    }

    public boolean isFetching() {
        return getPreferences().getBoolean(PreferenceKey.PREF_IS_FETCHING, true);
    }

    public void setFirstTime(boolean isFirstTime) {
        getPreferences().edit().putBoolean(PreferenceKey.PREF_IS_FIRST_TIME, isFirstTime).apply();
    }

    public boolean isFirstTime() {
        return getPreferences().getBoolean(PreferenceKey.PREF_IS_FIRST_TIME, true);
    }
}
