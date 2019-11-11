package com.example.smartredact.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.smartredact.common.constants.Constants
import timber.log.Timber

object SharedPreferencesUtils {

    private lateinit var mSharedPreferences: SharedPreferences

    fun init(context: Context) {
        mSharedPreferences = context.getSharedPreferences(Constants.SharedPreferences.NAME, Context.MODE_PRIVATE)
    }

    fun put(key: String, value: Boolean) {
        Timber.i("Writing pref key = $key value = $value")
        val editor = mSharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun put(key: String, value: Int) {
        Timber.i("Writing pref key = $key value = $value")
        val editor = mSharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun put(key: String, value: String?) {
        Timber.i("Writing pref key = $key value = $value")
        val editor = mSharedPreferences.edit()
        if (value == null) {
            editor.putString(key, "")
        } else {
            editor.putString(key, value)
        }
        editor.apply()
    }

    fun put(key: String, value: Long) {
        Timber.i("Writing pref key = $key value = $value")
        val editor = mSharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun clear(key: String) {
        Timber.i("Writing pref key = $key")
        val editor = mSharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun getInteger(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String): String {
        return mSharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    fun has(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }
}