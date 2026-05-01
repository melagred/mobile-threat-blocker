package com.example.safetyfirst.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VpnViewModel : ViewModel() {

    private val _vpnOn = MutableStateFlow(false)
    val vpnOn: StateFlow<Boolean> = _vpnOn.asStateFlow()

    fun setVpnOn(on: Boolean) {
        _vpnOn.value = on
    }

    private val _notifications = MutableStateFlow<List<String>>(emptyList())
    val notifications: StateFlow<List<String>> = _notifications

    fun addNotification(msg: String) {
        _notifications.value = _notifications.value + msg
    }

    private val _events = MutableStateFlow<List<GatewayEvent>>(emptyList())
    val events: StateFlow<List<GatewayEvent>> = _events.asStateFlow()

    fun refreshEvents() {
        ApiClient.fetchEvents { result ->
            if (result != null) _events.value = result
        }
    }
}
