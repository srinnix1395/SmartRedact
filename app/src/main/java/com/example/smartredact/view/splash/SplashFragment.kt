package com.example.smartredact.view.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.smartredact.R
import com.example.smartredact.view.home.HomeActivity
import com.google.android.exoplayer2.util.ColorParser
import kotlinx.android.synthetic.main.fragment_splash_screen.*


class SplashFragment : Fragment() {

    lateinit var splashViewPagerAdapter: SplashViewPagerAdapter
    var currentPage: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        splashViewPagerAdapter = SplashViewPagerAdapter(this.context!!)
        vpSplashScreen.adapter = splashViewPagerAdapter
        addBottomDot(currentPage)
        vpSplashScreen.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                addBottomDot(position)
                if (position == splashViewPagerAdapter.count - 1) {
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
            vpSplashScreen.currentItem = ++currentPage
        }
        btnGotIt.setOnClickListener {
            lauchHomeActivity()
        }
        btnSkip.setOnClickListener {
            lauchHomeActivity()
        }
    }

    private fun addBottomDot(currentPage: Int) {
        llDot.removeAllViews()
        for (i in 0 until splashViewPagerAdapter.count) {
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

    fun lauchHomeActivity(){
        val intent = Intent(this.activity, HomeActivity::class.java)
        startActivity(intent)
    }

}