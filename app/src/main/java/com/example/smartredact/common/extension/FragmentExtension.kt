package com.example.smartredact.common.extension

import android.app.Activity
import androidx.fragment.app.Fragment

fun Fragment.popAllBackStack() {
    val fm = childFragmentManager
    for (i in 0 until fm.backStackEntryCount) {
        fm.popBackStack()
    }
}

fun Fragment.addFragment(fragment: Fragment, root: Int, addToBackStack: Boolean) {
    var transaction = childFragmentManager
            .beginTransaction()
            .add(root, fragment, fragment.javaClass.name)

    if (addToBackStack) {
        transaction = transaction.addToBackStack(fragment.javaClass.name)
    }
    transaction.commit()
}


fun Fragment.replaceFragment(fragment: Fragment, root: Int, addToBackStack: Boolean) {
    var transaction = childFragmentManager
            .beginTransaction()
            .replace(root, fragment, fragment.javaClass.name)

    if (addToBackStack) {
        transaction = transaction.addToBackStack(fragment.javaClass.name)
    }
    transaction.commit()
}

fun Fragment.hideKeyBoardWhenTouchOutside(activity: Activity) {
    view?.hideKeyBoardWhenTouchOutside(activity)
}