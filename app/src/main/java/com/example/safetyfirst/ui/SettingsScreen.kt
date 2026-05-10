package com.example.safetyfirst.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    vpnViewModel: VpnViewModel = viewModel(),
    navbar: @Composable () -> Unit,
    aboutNavigate: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        settingsViewModel.connectionsUpdates = AppPrefs.getConnectionsUpdates(context)
    }
    LaunchedEffect(settingsViewModel.connectionsUpdates) {
        AppPrefs.setConnectionsUpdates(context, settingsViewModel.connectionsUpdates)
    }

    var showWeeklyReport by remember {
        mutableStateOf(false)
    }

    val highRisk by vpnViewModel.highRiskCount.collectAsState()
    val mediumRisk by vpnViewModel.mediumRiskCount.collectAsState()
    val safeConnections by vpnViewModel.safeConnectionCount.collectAsState()

    if (showWeeklyReport) {

        AlertDialog(

            onDismissRequest = {
                showWeeklyReport = false
            },

            title = {
                Text("Weekly Security Report")
            },

            text = {

                Column {

                    Text("High Risk Threats: $highRisk")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Medium Risk Threats: $mediumRisk")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Safe Connections: $safeConnections")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        if (settingsViewModel.weeklyReports)
                            "Analytics Retention: ENABLED"
                        else
                            "Analytics Retention: DISABLED"
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {
                        showWeeklyReport = false
                    }
                ) {

                    Text("Close")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .background(Color(0xFFC0C0C0))
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Settings",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3E63)
        )
        Text(
            text = "Customize your security preferences",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3E63)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = aboutNavigate,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )
            ) {
                Text(text = "About")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Use a helper function for your blocks
        SettingToggleButton(
            title = "VPN Auto-Start",
            description = "Automatically connect on app launch",
            isSelected = settingsViewModel.vpnAutoStart,
            onToggle = { settingsViewModel.toggleVpnAutoStart() }
        )
        SettingToggleButton(
            title = "Thread Alerts",
            description = "Get notified of security threats",
            isSelected = settingsViewModel.threadAlerts,
            onToggle = { settingsViewModel.toggleThreadAlerts() }
        )

        SettingToggleButton(
            title = "Connections Updates",
            description = "VPN connection status changes",
            isSelected = settingsViewModel.connectionsUpdates,
            onToggle = { settingsViewModel.toggleConnectionsUpdates() }
        )

        SettingToggleButton(
            title = "Weekly Reports",
            description = "Summary of security activity",
            isSelected = settingsViewModel.weeklyReports,
            onToggle = {

                settingsViewModel.toggleWeeklyReports()

                if (!settingsViewModel.weeklyReports) {
                    vpnViewModel.clearAnalytics()
                }
            }
        )

        if (settingsViewModel.weeklyReports) {

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        showWeeklyReport = true
                    },

                    shape = RoundedCornerShape(16.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2F3E63),
                        contentColor = Color.White
                    )
                ) {

                    Text("Generate Weekly Report")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        navbar()
    }
}



@Composable
fun SettingToggleButton(
    title: String,
    description: String,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f) // Text takes all space except the toggle
        ) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 14.sp, color = Color(0xFF2F3E63))
        }

        Switch(
            checked = isSelected,
            onCheckedChange = { onToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFC0C0C0),
                checkedTrackColor = Color(0xFF2F3E63),
                uncheckedThumbColor = Color.Gray
            )
        )
    }
}