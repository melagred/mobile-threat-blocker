package com.example.safetyfirst

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safetyfirst.ui.*
import androidx.core.content.edit
import com.example.safetyfirst.ui.common.Navbar

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    companion object {
        private const val VPN_REQUEST_CODE = 100
        private const val PREFS_NAME = "vpn_state"
        private const val KEY_VPN_ON = "vpn_on"
    }

    private val vpnViewModel: VpnViewModel by viewModels()
    private lateinit var vpnReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vpnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val msg = intent?.getStringExtra("msg") ?: return
                Log.d("VPN_DEBUG", "Received: $msg")
                vpnViewModel.addNotification(msg)
            }
        }

        registerReceiver(
            vpnReceiver,
            IntentFilter("VPN_NOTIF"),
            RECEIVER_NOT_EXPORTED
        )

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    200
                )
            }
        }

        enableEdgeToEdge()

        // Restore saved VPN UI state when app opens from notification
        val actuallyRunning = SafetyFirstVpnService.isRunning
        if (getSavedVpnState() && !actuallyRunning) {
            saveVpnState(false)
        }
        vpnViewModel.setVpnOn(actuallyRunning)

        if (AppPrefs.getAutoStart(this) && !actuallyRunning) {
            val prepareIntent = VpnService.prepare(this)
            if (prepareIntent != null) {
                Log.d("VPN_DEBUG", "Auto-start: requesting VPN permission")
                startActivityForResult(prepareIntent, VPN_REQUEST_CODE)
            } else {
                Log.d("VPN_DEBUG", "Auto-start: permission already granted, starting VPN")
                startVpnService()
            }
        }

        setContent {
            AppNavigation(vpnViewModel = vpnViewModel)
        }
    }

    private fun startVpnService() {
        val startIntent = Intent(this, SafetyFirstVpnService::class.java)
        startIntent.action = SafetyFirstVpnService.ACTION_START
        ContextCompat.startForegroundService(this, startIntent)

        saveVpnState(true)
        vpnViewModel.setVpnOn(true)
    }

    private fun saveVpnState(isOn: Boolean) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit {
                putBoolean(KEY_VPN_ON, isOn)
            }
    }

    private fun getSavedVpnState(): Boolean {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .getBoolean(KEY_VPN_ON, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(vpnReceiver)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VPN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startVpnService()
            } else {
                saveVpnState(false)
                vpnViewModel.setVpnOn(false)
            }
        }
    }
    @Composable
    fun AppNavigation(vpnViewModel: VpnViewModel) {
        navController = rememberNavController()

        val navbar: @Composable () -> Unit = {
            Navbar(
                dashboardNavigate = { navController.navigate(Routes.DashboardScreen) },
                threatsNavigate = { navController.navigate(Routes.ThreatsScreen) },
                settingsNavigate = { navController.navigate(Routes.SettingScreen) }
            )

        }

        NavHost(
            navController = navController,
            startDestination = Routes.DashboardScreen
        ) {


            composable(Routes.DashboardScreen) {
                DashboardScreen(
                    vpnViewModel = vpnViewModel,
                    navbar = navbar
                )
            }

            composable(Routes.ThreatsScreen) {
                ThreatsScreen(
                    threatsLogNavigate = { navController.navigate(Routes.ThreatLogScreen) },
                    navbar = navbar
                )
            }

            composable(Routes.SettingScreen) {
                SettingScreen(
                    navbar = navbar,
                    aboutNavigate = { navController.navigate(Routes.AboutScreen) }
                )
            }

            composable(Routes.ThreatLogScreen) {
                ThreatLogScreen(
                    navbar = navbar
                )
            }

            composable(Routes.AboutScreen) {
                AboutScreen(
                    DashsClick = { navController.navigate(Routes.DashboardScreen) },
                    ThreatsClick = { navController.navigate(Routes.ThreatsScreen) },
                    SettingsClick = { navController.navigate(Routes.SettingScreen) }
                )
            }
        }
    }

}

