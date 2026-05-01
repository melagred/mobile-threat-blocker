package com.example.safetyfirst.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginsViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVis by mutableStateOf(false)
    var isLoggedIn by mutableStateOf(false)
        private set
    var registrationState by mutableStateOf<RegistrationState>(RegistrationState.Idle)

    fun login() {
        //api
        isLoggedIn = true
    }

    private val _vpnOn = MutableStateFlow(false)
    val vpnOn: StateFlow<Boolean> = _vpnOn.asStateFlow()

    fun setVpnOn(on: Boolean) {
        _vpnOn.value = on
    }

    private val _events = MutableStateFlow<List<GatewayEvent>>(emptyList())
    val events: StateFlow<List<GatewayEvent>> = _events.asStateFlow()

    fun refreshEvents() {
        ApiClient.fetchEvents { result ->
            if (result != null) _events.value = result
        }
    }

    fun register() {
        //api

        // resets state
        registrationState = RegistrationState.Idle
        if (email.isNotBlank() && password.isNotBlank()) {
            // success
            registrationState = RegistrationState.Success

            //clear
            email = ""
            password = ""
        } else {
            //error message
            registrationState = RegistrationState.Error("Cant be empty")

        }
    }
}

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}