package com.example.smartredact.view.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.smartredact.R

class IntroViewPagerAdapter(private val mContext: Context) : PagerAdapter() {

    companion object {
        val layouts: IntArray = intArrayOf(
            R.layout.intro_page_1,
            R.layout.intro_page_2,
            R.layout.intro_page_3
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