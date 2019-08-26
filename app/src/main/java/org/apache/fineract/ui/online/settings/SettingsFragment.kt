package org.apache.fineract.ui.online.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.apache.fineract.R
import java.util.*


class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        super.onDisplayPreferenceDialog(preference)
        Log.d("TAG", "onDisplayPreferenece called")
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d("TAG", "preference value changed")
        var preference = findPreference<Preference>(key.toString())
        Log.d("TAG", Locale.getDefault().language+"------------------------------------------------->")
        if (preference is ListPreference) {
            var listPreference = preference
            Log.d("TAG", listPreference.value)
            Log.d("TAG", listPreference.key)

            val locale = Locale(listPreference.value)
            Locale.setDefault(locale)

            val resources = context?.getResources()

            val configuration = resources?.configuration
            configuration?.locale = locale

            resources?.updateConfiguration(configuration, resources.displayMetrics)

            var intent = Intent(activity, activity!!::class.java)
            startActivity(intent)
            activity?.finish()
            Log.d("TAG", configuration?.locale?.language)

        }
    }
}