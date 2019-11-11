package com.example.smartredact.common.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by TuHA on 6/6/2019.
 */

fun Context.isConnected(): Boolean {
    val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo? = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}