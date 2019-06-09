package org.apache.fineract.ui.online

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.apache.fineract.ui.online.launcher.LauncherActivity

/*
 * Created by saksham on 09/June/2019
*/

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, LauncherActivity::class.java))
    }
}