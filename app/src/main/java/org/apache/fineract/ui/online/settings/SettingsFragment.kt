package org.apache.fineract.ui.online.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper
import org.apache.fineract.R
import org.apache.fineract.ui.online.PassCodeActivity
import org.apache.fineract.utils.ConstantKeys
import org.apache.fineract.utils.LanguageUtils


class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)
        setupLanguagePreferenceSummery()
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
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference<Preference>(key.toString())
        if (preference is ListPreference) {
            context?.let {
                LanguageUtils.setLocale(it, preference.value)
                activity?.recreate()
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.password) -> {
                //    TODO("create changePasswordActivity and implement the logic for password change")
            }

            getString(R.string.passcode) -> {
                activity?.let {
                    val passCodePreferencesHelper = PasscodePreferencesHelper(activity)
                    val currPassCode = passCodePreferencesHelper.passCode
                    passCodePreferencesHelper.savePassCode("")
                    val intent = Intent(it, PassCodeActivity::class.java).apply {
                        putExtra(ConstantKeys.CURR_PASSWORD, currPassCode)
                        putExtra(ConstantKeys.IS_TO_UPDATE_PASS_CODE, true)
                    }
                    startActivity(intent)
                }

            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun setupLanguagePreferenceSummery() {
        findPreference<ListPreference>(getString(R.string.language))?.apply {
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val text = it?.entry
                if (text.isNullOrEmpty()) {
                    getString(R.string.choose_language)
                } else {
                    text
                }
            }
        }
    }


}