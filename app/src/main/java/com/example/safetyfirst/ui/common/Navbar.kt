package com.example.safetyfirst.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Navbar(dashboardNavigate: ()->Unit, threatsNavigate: ()->Unit, settingsNavigate: ()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .background(Color(0xFF2F3E63))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = dashboardNavigate,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2F3E63),
                contentColor = Color.White
            )) {
            Text("Dashboard")}
        Button(onClick =  threatsNavigate,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2F3E63),
                contentColor = Color.White
            )) {
            Text("Threats")}
        Button(onClick = settingsNavigate,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2F3E63),
                contentColor = Color.White
            )) {
            Text("Settings")}
    }
}