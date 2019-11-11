package com.example.smartredact.common.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.addFragment(fragment: Fragment, root: Int, addToBackStack: Boolean) {
    var transaction = supportFragmentManager
        .beginTransaction()
        .add(root, fragment, fragment.javaClass.name)

    if (addToBackStack) {
        transaction = transaction.addToBackStack(fragment.javaClass.name)
    }
    transaction.commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, root: Int, addToBackStack: Boolean) {
    var transaction = supportFragmentManager
        .beginTransaction()
        .replace(root, fragment, fragment.javaClass.name)

    if (addToBackStack) {
        transaction = transaction.addToBackStack(fragment.javaClass.name)
    }
    transaction.commit()
}

fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    val fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name
    return fragmentManager.findFragmentByTag(fragmentTag)
}

fun <T : Fragment> AppCompatActivity.findFragment(tag: String): T? {
    return supportFragmentManager.findFragmentByTag(tag) as? T
}

fun <T : Fragment> AppCompatActivity.findFragment(id: Int): T? {
    return supportFragmentManager.findFragmentById(id) as? T
}