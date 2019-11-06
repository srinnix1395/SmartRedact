package com.example.smartredact.view.splash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.smartredact.R

class SplashViewPagerAdapter(val mContext: Context) : PagerAdapter() {

    companion object {
        val layouts: IntArray = intArrayOf(
            R.layout.splash_screen_1,
            R.layout.splash_screen_2,
            R.layout.splash_screen_3
        )
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater : LayoutInflater = LayoutInflater.from(mContext)
        val view : View = inflater.inflate(layouts[position], container, false)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}