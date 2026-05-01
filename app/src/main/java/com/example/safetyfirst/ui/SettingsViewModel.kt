package com.example.safetyfirst.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    // Toggle states for your 4 blocks
    var vpnAutoStart by mutableStateOf(false)
    var threadAlerts by mutableStateOf(false)
    var connectionsUpdates by mutableStateOf(false)
    var weeklyReports by mutableStateOf(false)

    // Toggle functions
    fun toggleVpnAutoStart() { vpnAutoStart = !vpnAutoStart }
    fun toggleThreadAlerts() { threadAlerts = !threadAlerts }
    fun toggleConnectionsUpdates() { connectionsUpdates = !connectionsUpdates }
    fun toggleWeeklyReports() { weeklyReports = !weeklyReports }
}