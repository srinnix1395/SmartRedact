package com.example.smartredact.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartredact.SmartRedactApplication
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.common.di.module.ActivityModule

/**
 * Created by TuHA on 6/6/2019.
 */
abstract class BaseActivity : AppCompatActivity() {

    val smartRedactApplication: SmartRedactApplication
        get() {
            return application as SmartRedactApplication
        }

    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createActivityComponent()
        setupContentView()
        initView()
    }

    private fun setupContentView() {
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            setContentView(getLayoutId())
        }
    }

    private fun createActivityComponent() {
        activityComponent = smartRedactApplication
            .appComponent
            .activityComponentBuilder()
            .activityModule(ActivityModule(this))
            .build()
    }


    open fun initView(){

    }

    abstract fun getLayoutId(): Int
}