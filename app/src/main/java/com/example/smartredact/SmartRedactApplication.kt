package com.example.smartredact

import android.app.Application
import com.example.smartredact.common.di.component.AppComponent
import com.example.smartredact.common.di.component.DaggerAppComponent
import com.example.smartredact.common.di.module.ApplicationModule
import com.example.smartredact.data.local.SharedPreferencesUtils

/**
 * Created by TuHA on 11/11/2019.
 */
class SmartRedactApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initSharedPreferences()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    private fun initSharedPreferences() {
        SharedPreferencesUtils.init(this)
    }
}