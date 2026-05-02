package com.example.safetyfirst.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.safetyfirst.R
import com.example.safetyfirst.SafetyFirstVpnService
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun DashboardScreen(
    vpnViewModel: VpnViewModel,
    ThreatsClick: () -> Unit,
    SettingsClick: () -> Unit) {

    val context = LocalContext.current
    val activity = context.findActivity()
    val vpnOn by vpnViewModel.vpnOn.collectAsState()
    val events by vpnViewModel.events.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            vpnViewModel.refreshEvents()
            delay(3000)
        }
    }

    Column(modifier = Modifier
        .background(Color(0xFFC0C0C0))
        .fillMaxSize()
        ) {
        Spacer(modifier = Modifier.padding(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.safetyicon),
                contentDescription = "VPN Icon",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "Welcome to Safety First",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3E63)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(220.dp)
                .background(Color(0xFF2F3E63), shape = RoundedCornerShape(24.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (vpnOn) "PROTECTED" else "NOT PROTECTED",
                    fontSize = 26.sp,
                    color = if (vpnOn) Color.Green else Color.Red
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (!vpnOn) {
                            val intent = VpnService.prepare(context)
                            if (intent != null) {
                                Log.d("VPN_DEBUG", "Requesting VPN permission")
                                activity?.startActivityForResult(intent, 100)
                            } else {
                                Log.d("VPN_DEBUG", "Already approved, starting VPN")
                                startVpn(context)
                                vpnViewModel.setVpnOn(true)
                            }
                        } else {
                            stopVpn(context)
                            vpnViewModel.setVpnOn(false)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C8ED9),
                        contentColor = Color.White),
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth(0.7f)
                ) {
                    Icon(
                        imageVector = if (vpnOn) Icons.Default.PowerSettingsNew else Icons.Default.Lock,
                        contentDescription = "VPN Icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (vpnOn) "DISCONNECT" else "CONNECT",
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .background(Color(0xFFEAEAEA), shape = RoundedCornerShape(20.dp))
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            if (events.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        if (vpnOn) "Waiting for traffic..."
                        else "Turn on the VPN to see activity."
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    reverseLayout = true,
                ) {
                    items(events) { event -> EventRow(event) }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2F3E63))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )) {
                Text("Dashboard")}
            Button(onClick = ThreatsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )) {
                Text("Threats")}
            Button(onClick = SettingsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )) {
                Text("Settings")}
        }
    }
}


private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.US)

@Composable
private fun EventRow(event: GatewayEvent) {
    val color = when {
        event.verdict.startsWith("BLACKLIST") -> Color(0xFFB00020)
        event.verdict.startsWith("PHISHING") -> Color(0xFFB00020)
        event.verdict.startsWith("SUSPICIOUS") -> Color(0xFFB26A00)
        else -> Color(0xFF1B5E20)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = timeFormatter.format(Date(event.ts)),
            fontSize = 12.sp,
            color = Color.DarkGray,
        )
        Text(
            text = event.type,
            fontSize = 12.sp,
            color = Color.DarkGray,
        )
        Text(
            text = event.domain,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = event.verdict,
            fontSize = 12.sp,
            color = color,
        )
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
