package com.example.safetyfirst

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.safetyfirst.ui.*

class MainActivity : ComponentActivity() {
    private val vm: LoginsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

//        ApiClient.checkDomain("example.com") { result ->
//            Log.d("API_Test", "Response: $result")
//            runOnUiThread {
//                Toast.makeText(this, "Domain checked: $result", Toast.LENGTH_SHORT).show()
//            }
//        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                200
            )
        }
        enableEdgeToEdge()

        setContent {
            AppNavigation(vm)


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
fun AppNavigation(viewModel: LoginsViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.DashboardScreen){
        composable(Routes.DashboardScreen){
            DashboardScreen(
                viewModel = viewModel,
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
            SettingsScreen(
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
























