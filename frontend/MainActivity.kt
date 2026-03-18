package com.example.cse4550_login

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cse4550_login.ApiClient.checkDomain


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        ApiClient.checkDomain("example.com") { result ->
            Log.d("API_Test", "Response: $result")
        }


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
        setContent {
            AppNavigation()


        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            //start
            val startIntent = Intent(this, SafetyFirstVpnService::class.java)
            startIntent.action = SafetyFirstVpnService.ACTION_START
            ContextCompat.startForegroundService(this, startIntent)

            // update
            val vm: LoginsViewModel = ViewModelProvider(this)[LoginsViewModel::class.java]
            vm.setVpnOn(true)
        }
    }

}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.DashboardScreen){
        composable(Routes.DashboardScreen){
            DashboardScreen(
                ThreatsClick = {
                    navController.navigate(Routes.ThreatsScreen)
                },
                SettingsClick = {
                    navController.navigate(Routes.SettingScreen)
                }
            )
        }
        composable(Routes.ThreatsScreen){
            ThreatsScreen(
                DashsClick = {
                    navController.navigate(Routes.DashboardScreen)
                },
                ThreatsClick = {
                    navController.navigate(Routes.ThreatsScreen)
                },
                SettingsClick = {
                    navController.navigate(Routes.SettingScreen)
                },
                ThreatsLogClick = {
                    navController.navigate(Routes.ThreatLogScreen)
                }
            )
        }

        composable(Routes.SettingScreen){
            SettingScreen(
                DashsClick = {
                navController.navigate(Routes.DashboardScreen)
            },
                ThreatsClick = {
                    navController.navigate(Routes.ThreatsScreen)
                },
                AboutClick = {
                    navController.navigate(Routes.AboutScreen)
                })
        }

        composable(Routes.ThreatLogScreen){
            ThreatLogScreen(
                DashsClick = {
                    navController.navigate(Routes.DashboardScreen)
                },
                ThreatsClick = {
                    navController.navigate(Routes.ThreatsScreen)
                },
                SettingsClick = {
                    navController.navigate(Routes.SettingScreen)
                }
            )
        }

        composable(Routes.AboutScreen){
            AboutScreen(
                DashsClick = {
                    navController.navigate(Routes.DashboardScreen)
                },
                ThreatsClick = {
                    navController.navigate(Routes.ThreatsScreen)
                },
                SettingsClick = {
                    navController.navigate(Routes.SettingScreen)
                }
                )
        }

    }
}

