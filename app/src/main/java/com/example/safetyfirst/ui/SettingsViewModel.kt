package com.example.safetyfirst.ui


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    // Toggle states for your 4 blocks

    private val context = getApplication<Application>()

    val vpnAutoStartFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsKeys.VPN_AUTO_START] ?: false
        }
    fun SetAutoStartFlow(enabled: Boolean){
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[SettingsKeys.VPN_AUTO_START] = enabled
            }
        }
    }

    var threadAlerts by mutableStateOf(false)
    var connectionsUpdates by mutableStateOf(false)
    var weeklyReports by mutableStateOf(false)

    // Toggle functions
    fun toggleThreadAlerts() { threadAlerts = !threadAlerts }
    fun toggleConnectionsUpdates() { connectionsUpdates = !connectionsUpdates }
    fun toggleWeeklyReports() { weeklyReports = !weeklyReports }
}




// import androidx.compose.runtime.*
// import androidx.lifecycle.ViewModel

// class SettingsViewModel : ViewModel() {
//     // Toggle states for your 4 blocks
//     var vpnAutoStart by mutableStateOf(false)
//     var threadAlerts by mutableStateOf(false)
//     var connectionsUpdates by mutableStateOf(false)
//     var weeklyReports by mutableStateOf(false)

//     // Toggle functions
//     fun toggleVpnAutoStart() { vpnAutoStart = !vpnAutoStart }
//     fun toggleThreadAlerts() { threadAlerts = !threadAlerts }
//     fun toggleConnectionsUpdates() { connectionsUpdates = !connectionsUpdates }
//     fun toggleWeeklyReports() { weeklyReports = !weeklyReports }
// }
