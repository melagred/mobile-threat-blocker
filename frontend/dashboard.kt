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
    viewModel: LoginsViewModel,
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
            Text(text = if (vpnOn) "VPN is active and monitoring network traffic." else "Enable VPN protection to monitor network traffic and detect threats.")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (!vpnOn) {
                    val intent = VpnService.prepare(context)

                    if (intent != null){
                        Log.d("VPN_DEBUG", "Requesting VPN permission")
                        activity?.startActivityForResult(intent, 100)

                    } else {
                        Log.d("VPN_DEBUG", "Already approved, starting VPN")
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
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.net.VpnService
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.content.ContextCompat
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Lock
//import androidx.compose.material.icons.filled.PowerSettingsNew
//import androidx.compose.material3.Icon
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.Alignment
//
//
//@Composable
//fun DashboardScreen(
//    viewModel: LoginsViewModel,
//    ThreatsClick: () -> Unit,
//    SettingsClick: () -> Unit) {
//
//    val context = LocalContext.current
//    val activity = context.findActivity()
//    val vpnOn by viewModel.vpnOn.collectAsState()
//
//
//    Column(modifier = Modifier
//
//        .background(Color(0xFF415285))
//        .fillMaxSize()
//        ) {
//        Spacer(modifier = Modifier.padding(10.dp))
////        Image(
////            painter = painterResource(id = R.drawable.safetyicon),
////            contentDescription = "safetyicon"
////        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween, // pushes text left, image right
//            verticalAlignment = Alignment.CenterVertically   // aligns vertically in the row
//        ) {
//            // image on the left
//            Image(
//                painter = painterResource(id = R.drawable.safetyicon),
//                contentDescription = "VPN Icon",
//                modifier = Modifier
//                    .size(100.dp) // adjust size as needed
//            )
//            // Text on the right
//            Text(
//                text = "Welcome to Safety First",
//                fontSize = 20.sp,
//                color = Color.White
//            )
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .padding(top = 60.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .height(300.dp)
//                    .background(
//                        Color(0xFF2F3E63),
//                        shape = RoundedCornerShape(24.dp)
//                    )
//                    .padding(24.dp), 
//                contentAlignment = Alignment.Center
//            ) {
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//
//                    Text(
//                        text = if (vpnOn) "PROTECTED" else "NOT PROTECTED",
//                        fontSize = 26.sp,
//                        color = if (vpnOn) Color.Green else Color.Red
//                    )
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    Button(
//                        onClick = {
//                            if (!vpnOn) {
//                                val intent = VpnService.prepare(context)
//                                if (intent != null) {
//                                    Log.d("VPN_DEBUG", "Requesting VPN permission")
//                                    activity?.startActivityForResult(intent, 100)
//                                } else {
//                                    Log.d("VPN_DEBUG", "Already approved, starting VPN")
//                                    startVpn(context)
//                                    viewModel.setVpnOn(true)
//                                }
//                            } else {
//                                stopVpn(context)
//                                viewModel.setVpnOn(false)
//                            }
//                        },
//                        shape = RoundedCornerShape(40.dp),
//                        modifier = Modifier
//                            .height(80.dp)
//                            .fillMaxWidth(0.7f)
//                    ) {
//                        Icon(
//                            imageVector = if (vpnOn) Icons.Default.PowerSettingsNew else Icons.Default.Lock,
//                            contentDescription = "VPN Icon"
//                        )
//
//                        Spacer(modifier = Modifier.width(8.dp))
//
//                        Text(
//                            text = if (vpnOn) "DISCONNECT" else "CONNECT",
//                            fontSize = 18.sp
//                        )
//
//                    }
//                    Spacer(modifier = Modifier.weight(1f))
//                    Text(
//                        text = if (vpnOn)
//                            "Status: Secure connection active"
//                        else
//                            "Status: VPN is off",
//                        fontSize = 18.sp,
//                        color = Color.LightGray
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(30.dp))
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//                .background(
//                    Color(0xFFEAEAEA),
//                    shape = RoundedCornerShape(20.dp)
//                )
//                .padding(vertical = 30.dp, horizontal = 16.dp)
//        ) {
//            Text("notis")
//        }
//        Spacer(modifier = Modifier.weight(1f)) // pushes the next block to the bottom
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.LightGray)
//                .background(Color(0xFF415285))
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(onClick = {}) {
//                Text("Dashboard")}
//            Button(onClick =  ThreatsClick ) {
//                Text("Threats")}
//            Button(onClick = SettingsClick) {
//                Text("Settings")}
//        }
//    }
//}
//
//
//fun startVpn(context: Context) {
//    val intent = Intent(context, SafetyFirstVpnService::class.java)
//    intent.action = SafetyFirstVpnService.ACTION_START
//    ContextCompat.startForegroundService(context, intent)
//}
//
//fun stopVpn(context: Context) {
//    val intent = Intent(context, SafetyFirstVpnService::class.java)
//    intent.action = SafetyFirstVpnService.ACTION_STOP
//    ContextCompat.startForegroundService(context, intent)
//}
//
//
//fun Context.findActivity(): Activity? = when (this) {
//    is Activity -> this
//    is android.content.ContextWrapper -> baseContext.findActivity()
//    else -> null
//}


















