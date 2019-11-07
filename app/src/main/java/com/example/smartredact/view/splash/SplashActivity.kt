package com.example.smartredact.view.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.view.home.HomeActivity
import com.example.smartredact.view.intro.IntroActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences =
            getSharedPreferences(Constants.SHARED_PREFERENCES_SMART_REDACT, Context.MODE_PRIVATE)
        val intent = if (sharedPreferences.getBoolean(Constants.IS_FIRTS_LAUNCHER, true)) {
            sharedPreferences.edit().putBoolean(Constants.IS_FIRTS_LAUNCHER, false).apply()
            Intent(this, IntroActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
