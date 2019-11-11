package com.example.smartredact.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.data.local.SharedPreferencesUtils
import com.example.smartredact.view.home.HomeActivity
import com.example.smartredact.view.intro.IntroActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFirstLauncher = SharedPreferencesUtils.getBoolean(Constants.IS_FIRST_LAUNCHER, true)
        val intent = if (isFirstLauncher) {
            SharedPreferencesUtils.put(Constants.IS_FIRST_LAUNCHER, false)
            Intent(this, IntroActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
