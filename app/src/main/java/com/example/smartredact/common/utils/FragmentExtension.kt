package com.example.smartredact.common.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


fun FragmentManager?.addFragment(root: Int, newFragment: Fragment, args: Bundle?, fragmentByTag: String, enterAnim: Int = android.R.anim.fade_in, exitAnim: Int = android.R.anim.fade_out, addToBackStack: Boolean = true) {
    val transaction = this?.beginTransaction()
    transaction?.setCustomAnimations(enterAnim, exitAnim)
    if (addToBackStack) {
        transaction?.addToBackStack(fragmentByTag)
    }
    if (null != args) {
        newFragment.arguments = args
    }
    transaction?.add(root, newFragment, fragmentByTag)
    transaction?.commit()
}

fun FragmentManager?.replaceFragment(root: Int, newFragment: Fragment, args: Bundle?, fragmentByTag: String, enterAnim: Int = android.R.anim.fade_in, exitAnim: Int = android.R.anim.fade_out, addToBackStack: Boolean = true) {
    val transaction = this?.beginTransaction()
    transaction?.setCustomAnimations(enterAnim, exitAnim)
    if (addToBackStack) {
        transaction?.addToBackStack(fragmentByTag)
    }
    if (null != args) {
        newFragment.arguments = args
    }
    transaction?.replace(root, newFragment, fragmentByTag)
    transaction?.commit()
}