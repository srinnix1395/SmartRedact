package com.example.smartredact.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartredact.R
import com.example.smartredact.common.utils.addFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager.addFragment(HomeFragment(), null, HomeFragment::class.java.simpleName)
    }
}
