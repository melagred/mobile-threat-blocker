package com.example.cse4550_login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable

fun AboutScreen(
    DashsClick: () -> Unit,
    ThreatsClick: () -> Unit,
    SettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Terms of service",
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = SettingsClick,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "General")
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "About")
            }
        }


            val scrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(200.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 0.dp, max = 200.dp)
                            .verticalScroll(scrollState)

                    ) {
                        repeat(50) { index ->
                            Text(
                                text = "line $index: Terms of service...",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
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
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.text.font.FontWeight
//
//
//@Composable
//
//fun AboutScreen(
//    DashsClick: () -> Unit,
//    ThreatsClick: () -> Unit,
//    SettingsClick: () -> Unit
//) {
//    val scrollState = rememberScrollState()
//    Column(
//        modifier = Modifier
//            .background(Color.LightGray)
//            .background(Color(0xFFC0C0C0))
//            .fillMaxSize()
//    ) {
//        Spacer(modifier = Modifier.height(40.dp))
//        Text(
//            text = "Terms of service",
//            fontSize = 25.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF2F3E63)
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(
//                onClick = SettingsClick,
//                shape = RoundedCornerShape(16.dp),
//                modifier = Modifier.width(150.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2F3E63),
//                    contentColor = Color.White
//                )
//            ) {
//                Text(text = "General")
//            }
//            Button(
//                onClick = {},
//                shape = RoundedCornerShape(16.dp),
//                modifier = Modifier.width(150.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2F3E63),
//                    contentColor = Color.White
//                )
//            ) {
//                Text(text = "About")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp)
//                .padding(bottom = 20.dp)
//
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
//                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
//                    .padding(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .verticalScroll(scrollState)
//
//                ) {
//                    repeat(50) { index ->
//                        Text(
//                            text = "line $index: Terms of service...",
//                            fontSize = 20.sp,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//                    }
//                }
//            }
//        }
//
//
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
//                Text("Dashboard")}
//            Button(onClick =  ThreatsClick,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2F3E63),
//                    contentColor = Color.White
//                )) {
//                Text("Threats")}
//            Button(onClick = SettingsClick,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2F3E63),
//                    contentColor = Color.White
//                )) {
//                Text("Settings")}
//        }
//    }
//
//}
