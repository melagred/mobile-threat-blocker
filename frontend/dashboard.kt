import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun DashboardScreen(
    viewModel: LoginsViewModel = viewModel(),
    ThreatsClick: () -> Unit,
    SettingsClick: () -> Unit) {

    val context = LocalContext.current
    var vpnOn by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Welcome to Safety First",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(350.dp)
        ) {
            Text(text = if (vpnOn) "VPN Protection: ON" else "VPN Protection: OFF")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (!vpnOn) {
                    startVpn(context)
                    vpnOn = true
                } else {
                    stopVpn(context)
                    vpnOn = false
                }
            }) {
                Text(if (vpnOn) "Stop VPN" else "Start VPN")
            }


            Text("Content inside the gray block")
        }
        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Activity Log",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )


        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(350.dp)
        ) {
            Text("notis")
        }
        Spacer(modifier = Modifier.weight(1f)) // pushes the next block to the bottom

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {}) {
                Text("Dashboard")}
            Button(onClick =  ThreatsClick ) {
                Text("Threats")}
            Button(onClick = SettingsClick) {
                Text("Settings")}
        }
    }
    }


//fun startVpn(context: Context) {
   // val intent = VpnService.prepare(context)
  //  if (intent != null) {
  //      (context as? Activity)?.startActivityForResult(intent, 100)
  //  } else {
  //      val startIntent = Intent(context, SafetyFirstVpnService::class.java)
  //      startIntent.action = SafetyFirstVpnService.ACTION_START
  //      ContextCompat.startForegroundService(context, startIntent)
  //  }
//}

fun startVpn(context: Context) {
    val activity = context as? Activity
    if (activity == null) {
        Log.e("VPN", "Context is not an Activity, cannot request VPN permission")
        return
    }

    val prepareIntent = VpnService.prepare(activity)
    if (prepareIntent != null) {
        activity.startActivityForResult(prepareIntent, 100)
    } else {
        val startIntent = Intent(activity, SafetyFirstVpnService::class.java)
        startIntent.action = SafetyFirstVpnService.ACTION_START
        ContextCompat.startForegroundService(activity, startIntent)
    }
}

fun stopVpn(context: Context) {
    val stopIntent = Intent(context, SafetyFirstVpnService::class.java)
    stopIntent.action = SafetyFirstVpnService.ACTION_STOP
    ContextCompat.startForegroundService(context, stopIntent)
}
//fun stopVpn(context: Context) {
//    val stopInt = Intent(context, SafetyFirstVpnService::class.java)
//    stopInt.action = SafetyFirstVpnService.ACTION_STOP
//    context.startService(stopInt)
//}
