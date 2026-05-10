package com.example.safetyfirst.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable

fun ThreatLogScreen(
    navbar: @Composable () -> Unit,
) {
    // connects this screen to the VPN ViewModel
    val vpnViewModel = androidx.lifecycle.viewmodel.compose.viewModel<VpnViewModel>()

    // live events from the Azure gateway
    val events by vpnViewModel.events.collectAsState()

    // refreshes events while screen is open
    LaunchedEffect(Unit) {
        while (true) {
            vpnViewModel.refreshEvents()
            delay(10000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer(modifier = Modifier.padding(30.dp))

        Text(text = "Threat Log")

        // displays incoming threat events
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(events) { event ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {

                    Text(text = "Domain: ${event.domain}")

                    Text(text = "Verdict: ${event.verdict}")

                    Text(text = "Type: ${event.type}")

                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        navbar()
    }
}
