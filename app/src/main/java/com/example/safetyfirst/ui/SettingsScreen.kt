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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = viewModel(),
    DashsClick: () -> Unit,
    ThreatsClick: () -> Unit,
    AboutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .background(Color(0xFFC0C0C0))
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 0.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Settings",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3E63)
        )
        Text(
            text = "Customize your security preference",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3E63)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )
            ) {
                Text(text = "General")
            }
            Button(
                onClick = AboutClick,
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
            isSelected = viewModel.vpnAutoStart,
            onToggle = { viewModel.toggleVpnAutoStart() }
        )
        SettingToggleButton(
            title = "Thread Alerts",
            description = "Get notified of security threats",
            isSelected = viewModel.threadAlerts,
            onToggle = { viewModel.toggleThreadAlerts() }
        )

        SettingToggleButton(
            title = "Connections Updates",
            description = "VPN connection status changes",
            isSelected = viewModel.connectionsUpdates,
            onToggle = { viewModel.toggleConnectionsUpdates() }
        )

        SettingToggleButton(
            title = "Weekly Reports",
            description = "Summary of security activity",
            isSelected = viewModel.weeklyReports,
            onToggle = { viewModel.toggleWeeklyReports() }
        )
        Spacer(modifier = Modifier.weight(1f)) // pushes the next block to the bottom

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .background(Color(0xFF2F3E63))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = DashsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )) {
                Text("Dashboard")}
            Button(onClick =  ThreatsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )) {
                Text("Threats")}
            Button(onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )) {
                Text("Settings")}
        }
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

// import androidx.compose.foundation.background
// import androidx.compose.foundation.border
// import androidx.compose.foundation.layout.Arrangement
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.Spacer
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.width
// import androidx.compose.foundation.shape.RoundedCornerShape
// import androidx.compose.material3.Button
// import androidx.compose.material3.Switch
// import androidx.compose.material3.SwitchDefaults
// import androidx.compose.material3.Text
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.ui.unit.dp
// import androidx.compose.ui.unit.sp
// import androidx.lifecycle.viewmodel.compose.viewModel

// @Composable
// fun SettingsScreen(
//     viewModel: SettingsViewModel = viewModel(),
//     DashsClick: () -> Unit,
//     ThreatsClick: () -> Unit,
//     AboutClick: () -> Unit
// ) {
//     Column(
//         modifier = Modifier
//             .fillMaxSize()
//             .padding(horizontal = 16.dp, vertical = 0.dp)
//     ) {
//         Spacer(modifier = Modifier.height(40.dp))
//         Text(
//             text = "Settings",
//             fontSize = 20.sp
//         )
//         Text(
//             text = "Customize your security preference",
//             fontSize = 12.sp
//         )

//         Spacer(modifier = Modifier.height(16.dp))

//         Row(
//             modifier = Modifier.fillMaxWidth(),
//             horizontalArrangement = Arrangement.SpaceEvenly
//         ) {
//             Button(
//                 onClick = {},
//                 shape = RoundedCornerShape(16.dp),
//                 modifier = Modifier.width(150.dp)
//             ) {
//                 Text(text = "General")
//             }
//             Button(
//                 onClick = AboutClick,
//                 shape = RoundedCornerShape(16.dp),
//                 modifier = Modifier.width(150.dp)
//             ) {
//                 Text(text = "About")
//             }
//         }

//         Spacer(modifier = Modifier.height(24.dp))

//         // Use a helper function for your blocks
//         SettingToggleButton(
//             title = "VPN Auto-Start",
//             description = "Automatically connect on app launch",
//             isSelected = viewModel.vpnAutoStart,
//             onToggle = { viewModel.toggleVpnAutoStart() }
//         )
//         SettingToggleButton(
//             title = "Thread Alerts",
//             description = "Get notified of security threats",
//             isSelected = viewModel.threadAlerts,
//             onToggle = { viewModel.toggleThreadAlerts() }
//         )

//         SettingToggleButton(
//             title = "Connections Updates",
//             description = "VPN connection status changes",
//             isSelected = viewModel.connectionsUpdates,
//             onToggle = { viewModel.toggleConnectionsUpdates() }
//         )

//         SettingToggleButton(
//             title = "Weekly Reports",
//             description = "Summary of security activity",
//             isSelected = viewModel.weeklyReports,
//             onToggle = { viewModel.toggleWeeklyReports() }
//         )
//         Spacer(modifier = Modifier.weight(1f)) // pushes the next block to the bottom

//         Row(
//             modifier = Modifier
//                 .fillMaxWidth()
//                 .background(Color.Gray)
//                 .padding(8.dp),
//             horizontalArrangement = Arrangement.SpaceEvenly
//         ) {
//             Button(onClick = DashsClick) {
//                 Text("Dashboard")}
//             Button(onClick =  ThreatsClick ) {
//                 Text("Threats")}
//             Button(onClick = {}) {
//                 Text("Settings")}
//         }
//     }
// }



// @Composable
// fun SettingToggleButton(
//     title: String,
//     description: String,
//     isSelected: Boolean,
//     onToggle: (Boolean) -> Unit,
//     modifier: Modifier = Modifier
// ) {
//     Row (
//         modifier = modifier
//             .fillMaxWidth()
//             .padding(vertical = 8.dp, horizontal = 12.dp)
//             .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
//             .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
//             .padding(horizontal = 12.dp, vertical = 16.dp),
//         verticalAlignment = Alignment.CenterVertically
//     ) {
//         Column(
//             modifier = Modifier.weight(1f) // Text takes all space except the toggle
//         ) {
//             Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
//             Spacer(modifier = Modifier.height(4.dp))
//             Text(text = description, fontSize = 14.sp, color = Color.DarkGray)
//         }

//         Switch(
//             checked = isSelected,
//             onCheckedChange = { onToggle(it) },
//             colors = SwitchDefaults.colors(
//                 checkedThumbColor = Color(0xFF6200EE),
//                 uncheckedThumbColor = Color.Gray
//             )
//         )
//     }
//  }


