package com.example.cse4550_login


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashScreen : ViewModel() {
    private val _vpnOn = MutableStateFlow(false)
    val vpnOn: StateFlow<Boolean> = _vpnOn.asStateFlow()

    fun setVpnOn(on: Boolean) {
        _vpnOn.value = on
    }
}