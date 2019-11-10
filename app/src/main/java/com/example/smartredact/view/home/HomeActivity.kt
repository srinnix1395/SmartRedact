package com.example.smartredact.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartredact.common.utils.addFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.addFragment(android.R.id.content, HomeFragment(), null, HomeFragment::class.java.simpleName, addToBackStack = false)
    }
}
