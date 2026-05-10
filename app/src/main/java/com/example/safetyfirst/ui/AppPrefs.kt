package com.example.safetyfirst.ui

import android.content.Context

object AppPrefs {
    private const val FILE = "safetyfirst_prefs"
    private const val KEY_AUTO_START = "vpn_auto_start"
    private const val KEY_CONNECTIONS_UPDATES = "key_connections_updates"
    private const val KEY_WEEKLY_REPORTS = "weekly_reports"

    private const val KEY_THREAT_ALERTS = "threat_alerts"

    fun getAutoStart(context: Context): Boolean =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getBoolean(KEY_AUTO_START, false)

    fun setAutoStart(context: Context, value: Boolean) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_AUTO_START, value)
            .apply()
    }

    fun getWeeklyReports(
        context: Context
    ): Boolean =

        context.getSharedPreferences(
            FILE,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_WEEKLY_REPORTS, false)

    fun setWeeklyReports(
        context: Context,
        value: Boolean
    ) {

        context.getSharedPreferences(
            FILE,
            Context.MODE_PRIVATE
        )
            .edit()
            .putBoolean(KEY_WEEKLY_REPORTS, value)
            .apply()
    }

    fun getThreatAlerts(
        context: Context
    ): Boolean =

        context.getSharedPreferences(
            FILE,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_THREAT_ALERTS, false)

    fun setThreatAlerts(
        context: Context,
        value: Boolean
    ) {

        context.getSharedPreferences(
            FILE,
            Context.MODE_PRIVATE
        )
            .edit()
            .putBoolean(KEY_THREAT_ALERTS, value)
            .apply()
    }

    fun getConnectionsUpdates(context: Context): Boolean =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getBoolean(KEY_CONNECTIONS_UPDATES, false)

    fun setConnectionsUpdates(context: Context, value: Boolean) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_CONNECTIONS_UPDATES, value)
            .apply()
    }
}
