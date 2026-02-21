package com.example.safetyfirst;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> vpnPermissionLauncher;

    private SwitchCompat switchVpn;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 200);
            }
        }

        tvStatus = findViewById(R.id.tvStatus);
        switchVpn = findViewById(R.id.switchVpn);

        vpnPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        startVpn();
                    } else {
                        switchVpn.setChecked(false);
                        tvStatus.setText(getString(R.string.protection_off));
                    }
                }
        );

        switchVpn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) requestVpnPermission();
            else stopVpn();
        });
    }

    private void requestVpnPermission() {
        Intent prepareIntent = VpnService.prepare(this);
        if (prepareIntent != null) {
            vpnPermissionLauncher.launch(prepareIntent);
        } else {
            startVpn();
        }
    }

    private void startVpn() {
        Intent i = new Intent(this, SafetyFirstVpnService.class);
        i.setAction(SafetyFirstVpnService.ACTION_START);

        startForegroundService(i);

        tvStatus.setText(getString(R.string.protection_on));
    }

    private void stopVpn() {
        Intent i = new Intent(this, SafetyFirstVpnService.class);
        i.setAction(SafetyFirstVpnService.ACTION_STOP);
        startService(i);
        tvStatus.setText(getString(R.string.protection_off));
    }


}
