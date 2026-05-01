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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ThreatsScreen(
    viewModel: SettingsViewModel = viewModel(),
    DashsClick: () -> Unit,
    ThreatsClick: () -> Unit,
    SettingsClick: () -> Unit,
    ThreatsLogClick: () -> Unit
) {
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
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(0xBFFF0000),
                        shape = RoundedCornerShape(10.dp))
                    .height(100.dp)
                    .width(350.dp)
            ) {
                Text(text = "____--")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "MEDIUM RISK")
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(0xBFFF9800),
                        shape = RoundedCornerShape(10.dp))
                    .height(100.dp)
                    .width(350.dp)
            ) {
                Text(text = "Malware Detected")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "HIGH RISK")

            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(0xBF2196F3),
                        shape = RoundedCornerShape(10.dp))
                    .height(100.dp)
                    .width(350.dp)
            ) {
                Text(text = "____--")
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
                Button(onClick = {}) {
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
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
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
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.compose.ui.text.font.FontWeight
//
//
//@Composable
//fun ThreatsScreen(
//    viewModel: SettingsViewModel = viewModel(),
//    DashsClick: () -> Unit,
//    ThreatsClick: () -> Unit,
//    SettingsClick: () -> Unit,
//    ThreatsLogClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .background(Color.LightGray)
//            .background(Color(0xFFC0C0C0))
//            .fillMaxSize()
//    )
//    {
//        Spacer(modifier = Modifier.padding(40.dp))
//        Text(
//            text = "Detected Threats",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF2F3E63)
//        )
//        Text(
//            text = "Security incidents on your device",
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF2F3E63)
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(20.dp)
//                    .background(Color(0xBFFF0000),
//                        shape = RoundedCornerShape(10.dp))
//                    .height(100.dp)
//                    .width(350.dp)
//            ) {
//                Text(text = "____--")
//                Spacer(modifier = Modifier.weight(1f))
//                Text(text = "HIGH RISK")
//            }
//
//            Column(
//                modifier = Modifier
//                    .padding(20.dp)
//                    .background(Color(0xBFFF9800),
//                        shape = RoundedCornerShape(10.dp))
//                    .height(100.dp)
//                    .width(350.dp)
//            ) {
//                Text(text = "Malware Detected")
//                Spacer(modifier = Modifier.weight(1f))
//                Text(text = "MEDIUM RISK")
//
//            }
//
//            Column(
//                modifier = Modifier
//                    .padding(20.dp)
//                    .background(Color(0xBF2196F3),
//                      shape = RoundedCornerShape(10.dp))
//                    .height(100.dp)
//                    .width(350.dp)
//            ) {
//                Text(text = "____--")
//                Spacer(modifier = Modifier.weight(1f))
//                Text(text = "LOW RISK")
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(onClick = ThreatsLogClick,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF2F3E63),
//                        contentColor = Color.White
//                    )) {
//                    Text(text = "View Log")
//                }
//                Button(onClick = {},
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF2F3E63),
//                        contentColor = Color.White
//                    )) {
//                    Text(text = "Log summary")
//                }
//            }
//
//
//        }
//
//
//
//
//
//
//
//
//
//
//        Spacer(modifier = Modifier.weight(1f))
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF2F3E63))
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(onClick = DashsClick,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2F3E63),
//                    contentColor = Color.White
//                )) {
//                Text("Dashboard")
//            }
//            Button(onClick = ThreatsClick,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2F3E63),
//                    contentColor = Color.White
//                )) {
//                Text("Threats")
//            }
//            Button(onClick = SettingsClick,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2F3E63),
//                    contentColor = Color.White
//                )) {
//                Text("Settings")
//            }
//        }
//    }
//
//
//}
