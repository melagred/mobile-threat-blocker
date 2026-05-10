package com.example.safetyfirst.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun ThreatsScreen(
    viewModel: SettingsViewModel = viewModel(),
    DashsClick: () -> Unit,
    ThreatsClick: () -> Unit,
    SettingsClick: () -> Unit,
    ThreatsLogClick: () -> Unit
) {

    // connects to live VPN events
    val vpnViewModel = androidx.lifecycle.viewmodel.compose.viewModel<VpnViewModel>()

    // live events from Azure gateway
    val events by vpnViewModel.events.collectAsState()

    // refreshes events while screen is open
    LaunchedEffect(Unit) {
        while (true) {
            vpnViewModel.refreshEvents()
            delay(10000)
        }
    }

    // HIGH RISK threats
    val highRiskCount = events.count {
        it.verdict.contains("BLACKLIST", true) ||
                it.verdict.contains("MALWARE", true) ||
                it.verdict.contains("BLOCK", true)
    }

    // MEDIUM RISK threats
    val mediumRiskCount = events.count {
        it.verdict.contains("SUSPICIOUS", true) ||
                it.verdict.contains("WARN", true)
    }

    // LOW RISK / SAFE traffic
    val lowRiskCount = events.count {
        it.verdict.contains("SAFE", true)
    }

    // total logged events
    val totalEvents = events.size

    // latest event
    val latestEvent = events.lastOrNull()

    // most recent dangerous event
    val recentThreat = events.lastOrNull {
        !it.verdict.contains("SAFE", true)
    }

    // DNS event count
    val dnsCount = events.count {
        it.type.contains("DNS", true)
    }

    // SNI event count
    val sniCount = events.count {
        it.type.contains("SNI", true)
    }

    // controls popup visibility
    var showSummary by remember {
        mutableStateOf(false)
    }

    // summary popup
    if (showSummary) {

        AlertDialog(
            onDismissRequest = {
                showSummary = false
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        showSummary = false
                    }
                ) {
                    Text("Close")
                }
            },

            title = {
                Text("Threat Summary")
            },

            text = {

                Column {

                    Text("High Risk Threats: $highRiskCount")

                    Text("Medium Risk Threats: $mediumRiskCount")

                    Text("Safe Connections: $lowRiskCount")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Total Events Logged: $totalEvents")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("DNS Requests: $dnsCount")

                    Text("SNI Requests: $sniCount")

                    Spacer(modifier = Modifier.height(12.dp))

                    if (latestEvent != null) {

                        Text("Latest Domain: ${latestEvent.domain}")

                        Text("Latest Verdict: ${latestEvent.verdict}")

                        Text("Traffic Type: ${latestEvent.type}")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (recentThreat != null) {

                        Text(
                            text =
                                "Most Recent Threat: ${recentThreat.domain}"
                        )
                    }
                    else {

                        Text("No recent threats detected")
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {

        Spacer(modifier = Modifier.padding(40.dp))

        Text(
            text = "Detected Threats",
            fontSize = 20.sp
        )

        Text(
            text = "Security incidents on your device",
            fontSize = 12.sp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // HIGH RISK CARD
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(
                        Color(0xBFFF0000),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .height(100.dp)
                    .width(350.dp)
                    .padding(12.dp)
            ) {

                Text(text = "$highRiskCount Threats Detected")

                Spacer(modifier = Modifier.weight(1f))

                Text(text = "HIGH RISK")
            }

            // MEDIUM RISK CARD
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(
                        Color(0xBFFF9800),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .height(100.dp)
                    .width(350.dp)
                    .padding(12.dp)
            ) {

                Text(text = "$mediumRiskCount Threats Detected")

                Spacer(modifier = Modifier.weight(1f))

                Text(text = "MEDIUM RISK")
            }

            // LOW RISK CARD
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(
                        Color(0xBF2196F3),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .height(100.dp)
                    .width(350.dp)
                    .padding(12.dp)
            ) {

                Text(text = "$lowRiskCount Safe Connections")

                Spacer(modifier = Modifier.weight(1f))

                Text(text = "LOW RISK")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(onClick = ThreatsLogClick) {
                    Text(text = "View Log")
                }

                Button(
                    onClick = {
                        showSummary = true
                    }
                ) {
                    Text(text = "Log summary")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(onClick = DashsClick) {
                Text("Dashboard")
            }

            Button(onClick = ThreatsClick) {
                Text("Threats")
            }

            Button(onClick = SettingsClick) {
                Text("Settings")
            }
        }
    }
}