package com.example.safetyfirst.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable

fun AboutScreen(
    DashsClick: () -> Unit,
    ThreatsClick: () -> Unit,
    SettingsClick: () -> Unit
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
            color = Color(0xFF2F3E63)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = SettingsClick,
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
                onClick = {},
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
                    text = "Safety First : Privacy Policy \n" +
                            "1. Introduction\n" +
                            "Safety First is a mobile security application that provides network protection through VPN-based traffic routing and domain-level threat detection.\n" +
                            "T\n" +
                            "his Privacy Policy explains how your data is processed, used, and protected.\n" +
                            "\n" +
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
                            "\n" +
                            "3. Information We Do Not Collect\n" +
                            "We do not intentionally collect or store:\n" +
                            "- Browsing content (webpages, form inputs)\n" +
                            "- Messages, emails, or files\n" +
                            "- Account credentials or passwords\n" +
                            "- Personally identifiable information beyond network metadata\n" +
                            "\n" +
                            "\n" +
                            "4. How Data Is Used\n" +
                            "Data is used solely to:\n" +
                            "- Detect and block malicious domains\n" +
                            "- Analyze network threats\n" +
                            "- Improve system performance and accuracy\n" +
                            "- Maintain and secure the service" +
                            "Safety First : Security and Logging Policy\n" +
                            "\n" +
                            "Encryption:\n" +
                            "- All traffic between your device and Safety First servers is encrypted using industry-standard protocols (e.g., TLS).\n" +
                            "- DNS queries are transmitted securely to prevent interception.\n" +
                            "- Encryption is enforced for all supported connections.\n" +
                            "\n" +
                            "\n" +
                            "Logging Policy\n" +
                            "Safety First uses minimal logging for operational purposes.\n" +
                            "Logs may include:\n" +
                            "- Domain requests\n" +
                            "- Timestamps\n" +
                            "- Connection metadata\n" +
                            "\n" +
                            "Logs are used for:\n" +
                            "- Security monitoring\n" +
                            "- Debugging\n" +
                            "- Abuse prevention\n" +
                            "\n" +
                            "\n" +
                            "Log Retention\n" +
                            "Logs may be:\n" +
                            "- Stored temporarily\n" +
                            "- Retained for a limited duration\n" +
                            "- Automatically deleted after a defined period\n" +
                            "\n" +
                            "\n" +
                            "Logs are not:\n" +
                            "- Sold\n" +
                            "- Used for advertising\n" +
                            "- Intentionally linked to personal identity\n" +
                            "\n" +
                            "\n" +
                            "User Transparency\n" +
                            "We (the Safety First Team) aim to:\n" +
                            "- Minimize data collection\n" +
                            "- Limit retention duration\n" +
                            "- Provide clarity on how data is handled\n" +
                            "- Future versions may include user-facing controls for viewing or deleting logs." +
                            "TOS Terms of Service \n" +
                            "1. Acceptance of Terms\n" +
                            "\n" +
                            "By downloading, installing, or using Safety First, you agree to be bound by these Terms of Service. If you do not agree, you must not use the Safety First App.\n" +
                            "\n" +
                            "\n" +
                            "2. Description of Service\n" +
                            "\n" +
                            "Safety First is a mobile security application that provides domain-level threat detection and network protection by routing device traffic through secure VPN infrastructure.\n" +
                            "\n" +
                            "The App is designed to:\n" +
                            "\n" +
                            "- Analyze DNS requests and network metadata\n" +
                            "- Identify potentially malicious or suspicious domains\n" +
                            "- Block or warn against unsafe connections\n" +
                            "- Provide users with visibility into detected threats\n" +
                            "\n" +
                            "Disclaimer: The Safety First App is not full antivirus protection or guarantee complete security.\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "3. VPN and Traffic Routing\n" +
                            "\n" +
                            "Safety First uses your device’s VPN capabilities to route network traffic through remote servers operated or controlled by the service.\n" +
                            "\n" +
                            "By enabling the Safety First app:\n" +
                            "- You authorize routing of your network traffic through remote VPN servers\n" +
                            "- You understand that your traffic may be transmitted over external infrastructure\n" +
                            "- You acknowledge that a VPN indicator may appear on your device\n" +
                            "\n" +
                            "Traffic may be processed for the purpose of:\n" +
                            "- DNS resolution and filtering\n" +
                            "- Threat detection and classification\n" +
                            "- Service performance and reliability\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "4. Data Collection and Processing\n" +
                            "\n" +
                            "4.1 Data Processed\n" +
                            "\n" +
                            "The Safety First app may process:\n" +
                            "- DNS queries (domain names requested)\n" +
                            "- IP metadata (source/destination addresses)\n" +
                            "- Connection timestamps\n" +
                            "- Device-level network activity (limited to routing and detection purposes)\n" +
                            "\n" +
                            "\n" +
                            "4.2 Data Not Collected\n" +
                            "\n" +
                            "Safety First does not purposefully collect:\n" +
                            "- Browsing content (e.g., webpage text, form data)\n" +
                            "- Messages, emails, or personal files\n" +
                            "- User credentials or passwords\n" +
                            "\n" +
                            "\n" +
                            "4.3 Data Usage\n" +
                            "\n" +
                            "Data is used strictly to:\n" +
                            "- Detect and block malicious domains\n" +
                            "- Improve threat detection accuracy\n" +
                            "- Maintain and secure the service\n" +
                            "\n" +
                            "\n" +
                            "4.4 Data Retention\n" +
                            "\n" +
                            "Data may be:\n" +
                            "- Processed in real time\n" +
                            "- Temporarily stored for debugging or security purposes\n" +
                            "- Retained only as long as necessary for operational needs\n" +
                            "Note: Retention policies may change as the service evolves.\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "5. Privacy Considerations\n" +
                            "\n" +
                            "Use of a VPN requires routing traffic through external systems.\n" +
                            "\n" +
                            "You acknowledge that:\n" +
                            "- Network metadata must be processed to provide protection\n" +
                            "- Absolute anonymity cannot be guaranteed\n" +
                            "- Traffic may pass through infrastructure outside your device\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "6. Security Limitations\n" +
                            "\n" +
                            "Safety First provides best-effort protection only.\n" +
                            "\n" +
                            "By using the Safety First app, you as the user acknowledge that\n" +
                            "- Not all threats can be detected\n" +
                            "- Some malicious domains may bypass filtering\n" +
                            "- New threats may not yet be classified\n" +
                            "- VPN usage does not eliminate all cybersecurity risks\n" +
                            "\n" +
                            "The Safety First app is a supplementary security tool and should not be relied upon as a sole means of protection.\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "7. User Responsibilities\n" +
                            "\n" +
                            "You as the user agree to:\n" +
                            "- Use the Safety First app in compliance with all applicable laws\n" +
                            "- Not misuse the VPN service for illegal or unauthorized activity\n" +
                            "- Not attempt to interfere with or reverse engineer the Safety First app\n" +
                            "\n" +
                            "You as the user are solely responsible for your actions while using the Safety First app.\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "8. Third-Party Infrastructure and Data Sources\n" +
                            "\n" +
                            "The Safety First app may rely on:\n" +
                            "- Third-party hosting providers\n" +
                            "- External threat intelligence data sources\n" +
                            "\n" +
                            "We as providers do not guarantee:\n" +
                            "- Accuracy or completeness of third-party data\n" +
                            "- Continuous availability of external services\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "9. Service Availability\n" +
                            "\n" +
                            "The App may be modified, updated, interrupted, or discontinued at any time without notice.\n" +
                            "No guarantee is made regarding uptime or uninterrupted service.\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "10. Disclaimer of Warranties\n" +
                            "\n" +
                            "The Safety First app is provided “as is” and “as available”, without warranties of any kind, including:\n" +
                            "- Accuracy of threat detection\n" +
                            "- Reliability or availability\n" +
                            "- Fitness for a particular purpose\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "11. Limitation of Liability\n" +
                            "\n" +
                            "To the fullest extent permitted by law, the developers shall not be liable for:\n" +
                            "- Any direct or indirect damages\n" +
                            "- Security incidents not prevented by the Safety First app\n" +
                            "- Data loss or device issues\n" +
                            "- Consequences resulting from use of or reliance on the Safety First app\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "12. Termination\n" +
                            "\n" +
                            "We as providers reserve the right to suspend or terminate access to the Safety First app at any time, without notice, or for any reason.\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "13. Changes to These Terms\n" +
                            "\n" +
                            "These Terms may be updated periodically. Continued use of the Safety First app constitutes acceptance of any changes.\n" +
                            "\n" +
                            "\n" +
                            "14. Governing Law\n" +
                            "\n" +
                            "These Terms shall be governed by applicable laws in your relevant jurisdiction.\n" +
                            "\n" +
                            "\n" +
                            "15. Contact Information\n" +
                            "\n" +
                            "[Safety First Team / CSUSB]\n" +
                            "[Jennifer.Jin@csusb.edu]",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2F3E63))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = DashsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )
            ) {
                Text("Dashboard")
            }
            Button(
                onClick = ThreatsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )
            ) {
                Text("Threats")
            }
            Button(
                onClick = SettingsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F3E63),
                    contentColor = Color.White
                )
            ) {
                Text("Settings")
            }
        }
    }
}


// import androidx.compose.foundation.background
// import androidx.compose.foundation.border
// import androidx.compose.foundation.layout.Arrangement
// import androidx.compose.foundation.layout.Box
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.Spacer
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.heightIn
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.width
// import androidx.compose.foundation.rememberScrollState
// import androidx.compose.foundation.shape.RoundedCornerShape
// import androidx.compose.foundation.verticalScroll
// import androidx.compose.material3.Button
// import androidx.compose.material3.Text
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.unit.dp
// import androidx.compose.ui.unit.sp

// @Composable

// fun AboutScreen(
//     DashsClick: () -> Unit,
//     ThreatsClick: () -> Unit,
//     SettingsClick: () -> Unit
// ) {
//     Column(
//         modifier = Modifier
//             .fillMaxSize()
//     ) {
//         Spacer(modifier = Modifier.height(40.dp))
//         Text(
//             text = "Terms of service",
//             fontSize = 20.sp
//         )
//         Spacer(modifier = Modifier.height(30.dp))

//         Row(
//             modifier = Modifier.fillMaxWidth(),
//             horizontalArrangement = Arrangement.SpaceEvenly
//         ) {
//             Button(
//                 onClick = SettingsClick,
//                 shape = RoundedCornerShape(16.dp),
//                 modifier = Modifier.width(150.dp)
//             ) {
//                 Text(text = "General")
//             }
//             Button(
//                 onClick = {},
//                 shape = RoundedCornerShape(16.dp),
//                 modifier = Modifier.width(150.dp)
//             ) {
//                 Text(text = "About")
//             }
//         }


//             val scrollState = rememberScrollState()

//             Box(
//                 modifier = Modifier
//                     .fillMaxSize(),
//                 contentAlignment = Alignment.Center
//             ) {
//                 Box(
//                     modifier = Modifier
//                         .width(300.dp)
//                         .height(200.dp)
//                         .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
//                         .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
//                         .padding(12.dp)
//                 ) {
//                     Column(
//                         modifier = Modifier
//                             .fillMaxWidth()
//                             .heightIn(min = 0.dp, max = 200.dp)
//                             .verticalScroll(scrollState)

//                     ) {
//                         repeat(50) { index ->
//                             Text(
//                                 text = "line $index: Terms of service...",
//                                 fontSize = 20.sp,
//                                 modifier = Modifier.padding(bottom = 4.dp)
//                             )
//                         }
//                     }
//                 }
//             }


//             Spacer(modifier = Modifier.weight(1f))
//             Row(
//                 modifier = Modifier
//                     .fillMaxWidth()
//                     .background(Color.Gray)
//                     .padding(8.dp),
//                 horizontalArrangement = Arrangement.SpaceEvenly
//             ) {
//                 Button(onClick = DashsClick) {
//                     Text("Dashboard")
//                 }
//                 Button(onClick = ThreatsClick) {
//                     Text("Threats")
//                 }
//                 Button(onClick = SettingsClick) {
//                     Text("Settings")
//                 }
//             }
//         }

//     }
