package com.example.cse4550_login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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
    private val _vpnOn = MutableStateFlow(false)
    val vpnOn: StateFlow<Boolean> = _vpnOn.asStateFlow()

    fun setVpnOn(on: Boolean) {
        _vpnOn.value = on
    }
}

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}
