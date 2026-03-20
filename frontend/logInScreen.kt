package com.example.cse4550_login


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation




@Composable
fun LoginScreen(authVM: LoginsViewModel, loginSuccess: () -> Unit, goToRegister: ()-> Unit){



    //val icon = if(passwordVis)
        //painterResource(id = R.drawable.baseline_remove_red_eye_24)
    //else
        //painterResource(id = R.drawable.baseline_remove_red_eye_24)



    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.s), contentDescription = "Login image")

        Text(text = "Welcome to")
        Text(text = "Safety First")
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(authVM.email, onValueChange = {
            authVM.email = it
        }, label = {
            Text(text = "Email address")
        })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(authVM.password, onValueChange = {
            authVM.password = it
        }, label = {
            Text(text = "Password")
        }, visualTransformation = if (authVM.passwordVis) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }

        )
            Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                loginSuccess()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.Black

            )) {
            Text(text = "Login")
        }
        Row() {
            TextButton(
                onClick = { },) {
                Text(text = "forgot password?",
                    color = Color.LightGray)
            }
            TextButton(onClick = goToRegister) {
                Text(text = "New? Sign up!")
            }
        }
    }

}