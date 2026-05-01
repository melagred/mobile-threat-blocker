package com.example.safetyfirst.ui

import android.content.Context

object AppPrefs {
    private const val FILE = "safetyfirst_prefs"
    private const val KEY_AUTO_START = "vpn_auto_start"

    fun getAutoStart(context: Context): Boolean =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getBoolean(KEY_AUTO_START, false)

    fun setAutoStart(context: Context, value: Boolean) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_AUTO_START, value)
            .apply()
    }
}
