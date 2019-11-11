package com.example.smartredact.view.intro

import com.example.smartredact.common.extension.addFragment
import com.example.smartredact.common.extension.findFragment
import com.example.smartredact.view.base.BaseActivity

class IntroActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return 0
    }

    override fun initView() {
        addFragment(IntroFragment(), android.R.id.content, false)
    }

    override fun onBackPressed() {
        val handle = findFragment<IntroFragment>(IntroFragment::class.java.name)?.onBackPressed()
        if (handle != true) {
            super.onBackPressed()
        }
    }
}
