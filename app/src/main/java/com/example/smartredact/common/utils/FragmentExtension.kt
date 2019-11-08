package com.example.smartredact.common.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.smartredact.R


fun FragmentManager?.addFragment(newFragment: Fragment, args: Bundle?, fragmentByTag: String, enterAnim: Int = android.R.anim.fade_in, exitAnim: Int = android.R.anim.fade_out, addToBackStack: Boolean = true) {
    val transaction = this?.beginTransaction()
    transaction?.setCustomAnimations(enterAnim, exitAnim)
    if (addToBackStack) {
        transaction?.addToBackStack(fragmentByTag)
    }
    if (null != args) {
        newFragment.arguments = args
    }
    transaction?.add(R.id.frame_layout_home, newFragment, fragmentByTag)
    transaction?.commit()
}

fun FragmentManager?.replaceFragment(newFragment: Fragment, args: Bundle?, fragmentByTag: String, enterAnim: Int = android.R.anim.fade_in, exitAnim: Int = android.R.anim.fade_out, addToBackStack: Boolean = true) {
    val transaction = this?.beginTransaction()
    transaction?.setCustomAnimations(enterAnim, exitAnim)
    if (addToBackStack) {
        transaction?.addToBackStack(fragmentByTag)
    }
    if (null != args) {
        newFragment.arguments = args
    }
    transaction?.replace(R.id.frame_layout_home, newFragment, fragmentByTag)
    transaction?.commit()
}