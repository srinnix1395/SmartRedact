package com.example.smartredact.view.home

import com.example.smartredact.common.extension.addFragment
import com.example.smartredact.view.base.BaseActivity

class HomeActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return 0
    }

    override fun initView() {
        addFragment(HomeFragment(), android.R.id.content, false)
    }
}
