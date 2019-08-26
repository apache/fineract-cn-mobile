package org.apache.fineract.ui.online

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mifos.mobile.passcode.MifosPassCodeActivity
import com.mifos.mobile.passcode.utils.EncryptionUtil
import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper
import org.apache.fineract.R
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.login.LoginActivity
import org.apache.fineract.utils.ConstantKeys


/*
 * Created by saksham on 12/June/2019
*/

class PassCodeActivity : MifosPassCodeActivity() {

    private var currPassCode: String? = null
    private var isToUpdatePassCode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let {
            currPassCode = it.getStringExtra(ConstantKeys.CURR_PASSWORD)
            isToUpdatePassCode = it.getBooleanExtra(ConstantKeys.IS_TO_UPDATE_PASS_CODE, false)
        }
    }

    override fun showToaster(view: View?, msg: Int) {
        Toaster.show(view, msg, Toaster.SHORT)
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun getLogo(): Int {
        return R.drawable.mifos_logo_new
    }

    override fun getEncryptionType(): Int {
        return EncryptionUtil.FINERACT_CN
    }

    override fun startNextActivity() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isToUpdatePassCode && !currPassCode.isNullOrEmpty()) {
            PasscodePreferencesHelper(this).apply {
                savePassCode(currPassCode)
            }
        }
        finish()
    }
}