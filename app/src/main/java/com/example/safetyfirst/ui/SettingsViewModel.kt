package com.example.safetyfirst.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    var vpnAutoStart by mutableStateOf(AppPrefs.getAutoStart(application))
    var threadAlerts by mutableStateOf(false)
    var connectionsUpdates by mutableStateOf(false)
    var weeklyReports by mutableStateOf(false)

    fun toggleVpnAutoStart() {
        vpnAutoStart = !vpnAutoStart
        AppPrefs.setAutoStart(getApplication(), vpnAutoStart)
    }

    fun toggleThreadAlerts() { threadAlerts = !threadAlerts }
    fun toggleConnectionsUpdates() { connectionsUpdates = !connectionsUpdates }
    fun toggleWeeklyReports() { weeklyReports = !weeklyReports }
}
