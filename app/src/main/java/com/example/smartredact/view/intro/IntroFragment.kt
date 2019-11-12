package com.example.smartredact.view.intro

import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.smartredact.R
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.data.local.SharedPreferencesUtils
import com.example.smartredact.view.base.BaseFragment
import com.example.smartredact.view.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : BaseFragment() {

    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private var currentPage: Int = 0

    override fun inject(activityComponent: ActivityComponent) {
        //do nothing
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_intro
    }

    override fun initView() {
        introViewPagerAdapter = IntroViewPagerAdapter(this.context!!)
        vpIntroScreen.adapter = introViewPagerAdapter
        addBottomDot(currentPage)
        vpIntroScreen.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                addBottomDot(position)
                if (position == introViewPagerAdapter.count - 1) {
                    btnSkip.visibility = View.INVISIBLE
                    btnNext.visibility = View.GONE
                    btnGotIt.visibility = View.VISIBLE
                } else {
                    btnSkip.visibility = View.VISIBLE
                    btnGotIt.visibility = View.GONE
                    btnNext.visibility = View.VISIBLE
                }
            }

        })
        btnNext.setOnClickListener {
            vpIntroScreen.currentItem = ++currentPage
        }
        btnGotIt.setOnClickListener {
            launchHomeActivity()
        }
        btnSkip.setOnClickListener {
            launchHomeActivity()
        }
    }

    private fun addBottomDot(currentPage: Int) {
        llDot.removeAllViews()
        for (i in 0 until introViewPagerAdapter.count) {
            val dot = TextView(this.context)
            dot.text = Html.fromHtml("&#8226;")
            dot.textSize = 40F
            if (i == currentPage) {
                dot.setTextColor(Color.parseColor("#2278d4"))
            } else {
                dot.setTextColor(Color.parseColor("#93c6fd"))
            }
            llDot.addView(dot)
        }
    }

    private fun launchHomeActivity() {
        SharedPreferencesUtils.put(Constants.IS_FIRST_LAUNCHER, false)
        val intent = Intent(this.activity, HomeActivity::class.java)
        startActivity(intent)
        this.activity?.finish()
    }

    override fun onBackPressed(): Boolean {
        if(currentPage == 0){
            this.activity?.finish()
        } else{
            vpIntroScreen.currentItem = --currentPage
        }
        return true
    }
}