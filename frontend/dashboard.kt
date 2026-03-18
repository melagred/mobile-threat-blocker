package com.example.cse4550_login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState


@Composable
fun DashboardScreen(
    viewModel: LoginsViewModel = viewModel(),
    ThreatsClick: () -> Unit,
    SettingsClick: () -> Unit) {

    val context = LocalContext.current
    val activity = context.findActivity()
    val vpnOn by viewModel.vpnOn.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Welcome to Safety First",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(350.dp)
        ) {
            Text(text = if (vpnOn) "VPN Protection: ON" else "VPN Protection: OFF")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (!vpnOn) {
                    val intent = VpnService.prepare(context)
                    if (intent != null){
                        activity?.startActivityForResult(intent, 100)
                    } else {
                        startVpn(context)
                        viewModel.setVpnOn(true)
                    }

                } else {
                    stopVpn(context)
                    viewModel.setVpnOn(false)
                }
            }) {
                Text(if (vpnOn) "Stop VPN" else "Start VPN")
            }



            Text("Content inside the gray block")
        }
        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Activity Log",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )


        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(350.dp)
        ) {
            Text("notis")
        }
        Spacer(modifier = Modifier.weight(1f)) // pushes the next block to the bottom

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {}) {
                Text("Dashboard")}
            Button(onClick =  ThreatsClick ) {
                Text("Threats")}
            Button(onClick = SettingsClick) {
                Text("Settings")}
        }
    }
    }


fun startVpn(context: Context) {
    val intent = Intent(context, SafetyFirstVpnService::class.java)
    intent.action = SafetyFirstVpnService.ACTION_START
    ContextCompat.startForegroundService(context, intent)
}

fun stopVpn(context: Context) {
    val intent = Intent(context, SafetyFirstVpnService::class.java)
    intent.action = SafetyFirstVpnService.ACTION_STOP
    ContextCompat.startForegroundService(context, intent)
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}

















