package com.example.safetyfirst.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.safetyfirst.R

@Composable
fun RegisterScreen(authVm: LoginsViewModel, RegisterSuccess: ()-> Unit, GoToLogin: ()-> Unit){
    Column(modifier = Modifier.fillMaxSize().padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.safetyicon), contentDescription = "Login image")
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(authVm.email, onValueChange = {
            authVm.email = it},
            label = {Text(text = "email")
        })

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(authVm.password, onValueChange = {
            authVm.password = it},
            label = {Text(text = "password")},
            visualTransformation = PasswordVisualTransformation())


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    GoToLogin()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black

                )) {
                Text(text = "Login Page")
            }

            Button(
                onClick = { authVm.register()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black
                )) {
                Text(text = "Register")
            }


        }
        if (authVm.registrationState is RegistrationState.Error) {
            Text(
                text = (authVm.registrationState as RegistrationState.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Automatically navigate on success
        if (authVm.registrationState is RegistrationState.Success) {
            LaunchedEffect(Unit) {
                RegisterSuccess() // or GoToLogin() depending on your navigation
            }
        }

    }
}