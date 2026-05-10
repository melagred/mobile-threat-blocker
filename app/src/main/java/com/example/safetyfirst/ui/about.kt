package com.example.safetyfirst.ui

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable

fun AboutScreen(
    backClick: () -> Unit
) {

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .background(Color(0xFFC0C0C0))
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Terms of service",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3E63),
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3E63)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Back button only
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = backClick,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Back")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                .padding(12.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)

            ) {
                Text(
                    text =
                        "Safety First : Privacy Policy \n" +
                                "\n" +
                                "1. Introduction\n" +
                                "Safety First is a mobile security application that provides network protection through VPN-based traffic routing and domain-level threat detection.\n" +
                                "\n" +
                                "This Privacy Policy explains how your data is processed, used, and protected.\n" +
                                "\n" +
                                "2. Information We Process\n" +
                                "The Safety First app may process:\n" +
                                "- DNS queries (domains requested)\n" +
                                "- IP addresses (source and destination)\n" +
                                "- Connection timestamps\n" +
                                "- Device/network metadata necessary for routing and detection\n" +
                                "\n" +
                                "This data is required for the Safety First app to function.\n" +
                                "\n" +
                                "3. Information We Do Not Collect\n" +
                                "We do not intentionally collect or store:\n" +
                                "- Browsing content (webpages, form inputs)\n" +
                                "- Messages, emails, or files\n" +
                                "- Account credentials or passwords\n" +
                                "- Personally identifiable information beyond network metadata\n" +
                                "\n" +
                                "4. How Data Is Used\n" +
                                "Data is used solely to:\n" +
                                "- Detect and block malicious domains\n" +
                                "- Analyze network threats\n" +
                                "- Improve system performance and accuracy\n" +
                                "- Maintain and secure the service\n" +
                                "\n" +
                                "TOS Terms of Service\n" +
                                "\n" +
                                "1. Acceptance of Terms\n" +
                                "By downloading, installing, or using Safety First, you agree to be bound by these Terms of Service.\n" +
                                "\n" +
                                "2. Description of Service\n" +
                                "Safety First is a mobile security application that provides domain-level threat detection and network protection by routing device traffic through secure VPN infrastructure.\n" +
                                "\n" +
                                "3. VPN and Traffic Routing\n" +
                                "Safety First uses your device’s VPN capabilities to route network traffic through remote servers.\n" +
                                "\n" +
                                "4. Security Limitations\n" +
                                "Safety First provides best-effort protection only.\n" +
                                "\n" +
                                "5. User Responsibilities\n" +
                                "Users agree not to misuse the VPN service or attempt to reverse engineer the application.\n" +
                                "\n" +
                                "6. Disclaimer\n" +
                                "Safety First is provided 'as is' without guarantees of complete security.\n" +
                                "\n" +
                                "7. Contact Information\n" +
                                "[Safety First Team / CSUSB]\n" +
                                "[Jennifer.Jin@csusb.edu]",

                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}