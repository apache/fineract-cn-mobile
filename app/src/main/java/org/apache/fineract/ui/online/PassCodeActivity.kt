package org.apache.fineract.ui.online

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.mifos.mobile.passcode.MifosPassCodeActivity
import com.mifos.mobile.passcode.utils.EncryptionUtil
import org.apache.fineract.R
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.login.LoginActivity


/*
 * Created by saksham on 12/June/2019
*/

class PassCodeActivity : MifosPassCodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
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
}