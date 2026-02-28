 package com.example.cse4550_login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingScreen(
    DashsClick: () -> Unit,
    ThreatsClick: () -> Unit,
    AboutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 0.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Settings",
            fontSize = 20.sp
        )
        Text(
            text = "Customize your security preference",
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "General")
            }
            Button(
                onClick = AboutClick,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "About")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Use a helper function for your blocks
        SettingBlock(
            title = "VPN Auto-Start",
            subtitle = "Automatically connect on app launch"
        )
        SettingBlock(
            title = "Thread Alerts",
            subtitle = "Get notified of security threats"
        )
        SettingBlock(
            title = "Connections Updates",
            subtitle = "VPN connection status changes"
        )
        SettingBlock(
            title = "Weekly Reports",
            subtitle = "Summary of security activity"
        )
        Spacer(modifier = Modifier.weight(1f)) // pushes the next block to the bottom

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = DashsClick) {
                Text("Dashboard")}
            Button(onClick =  ThreatsClick ) {
                Text("Threats")}
            Button(onClick = {}) {
                Text("Settings")}
        }
    }
}


@Composable
fun SettingBlock(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .width(380.dp)
            .padding(12.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
            .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 18.sp)
        Text(text = subtitle, fontSize = 14.sp)
    }
}

@Composable
fun BlockToggleButton(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFF6200EE) else Color.LightGray
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                color = if (isSelected) Color.White else Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = if (isSelected) Color.White else Color.DarkGray,
                fontSize = 12.sp
            )
        }
    }
 }
