package com.example.smartredact.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartredact.R
import com.example.smartredact.common.utils.addFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager.addFragment(HomeFragment(), null, HomeFragment::class.java.simpleName, addToBackStack = false)
    }
}
