package com.example.cse4550_login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()




        }
    }
}


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val authVM: LoginsViewModel = viewModel()
    NavHost(
        navController = navController, startDestination = Routes.LoginScreen) {
        composable(Routes.LoginScreen) {
            LoginScreen(
                authVM = authVM,
                loginSuccess = { navController.navigate(Routes.DashboardScreen) },
                goToRegister = { navController.navigate(Routes.RegisterScreen) }
            )
        }
        composable(Routes.DashboardScreen){
            DashboardScreen()
        }
        composable(Routes.RegisterScreen){
            RegisterScreen(
                authVm = authVM,
                RegisterSuccess = { navController.navigate(Routes.LoginScreen)},
                GoToLogin = { navController.popBackStack()}
            )
        }
    }
}

