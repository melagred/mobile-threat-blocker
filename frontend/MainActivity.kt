package com.example.cse4550_login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val startIntent = Intent(this, SafetyFirstVpnService::class.java)
            startIntent.action = SafetyFirstVpnService.ACTION_START
            startService(startIntent)
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




