package com.example.safetyfirst;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import kotlin.text.HexFormat;

public class SafetyFirstVpnService extends VpnService {

    private static final String TAG = "SafetyFirstVpnService";

    public static final String ACTION_START = "com.example.safetyfirst.action.START";
    public static final String ACTION_STOP = "com.example.safetyfirst.action.STOP";

    private static final String CHANNEL_ID = "safetyfirst_vpn_channel";

    private static final int FOREGROUND_ID = 1;

    private ParcelFileDescriptor vpnInterface;

    private boolean isOn;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) return START_STICKY;

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand action=" + action);

        if (ACTION_START.equals(action)) {
            // Must happen immediately for a foreground service
            isOn = true;
            updateForegroundNotification("Protection is ON");
            Log.d("SafetyFirstVpnService", "establishing");
            establishMinimalVpn();
        } else if (ACTION_STOP.equals(action)) {
            isOn = false;
            stopVpn();
        }

        return START_STICKY;
    }

    private void updateForegroundNotification(String text) {
        createChannelIfNeeded();

        Intent openApp = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                openApp,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_lock)
                .setContentTitle("Safety First")
                .setContentText(text)
                .setOngoing(true)
                .setAutoCancel(false)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(pi)
                .build();
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            startForeground(
                    FOREGROUND_ID,
                    notification,
                    android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            );
        } else {
            startForeground(FOREGROUND_ID, notification);
        }
        startForeground(FOREGROUND_ID, notification);
        Log.d(TAG, "startForeground called");
    }

    private void establishMinimalVpn() {
        if (vpnInterface != null) return;

        Builder builder = new Builder();

        vpnInterface = builder.setSession("SafetyFirst VPN")
                .addAddress("192.168.2.2", 24)
                .addRoute("0.0.0.0", 0)
                .establish();
        if (vpnInterface == null) {
            Log.e(TAG, "Failed to establish VPN. Permission denied or another VPN active.");
        } else {
            Log.d(TAG, "VPN established ");
        }
        new Thread(() -> {
                runsocket(vpnInterface);
        }).start();
    }

    private void stopVpn() {
        Log.d(TAG, "Stopping VPN");
        try {
            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing vpnInterface", e);
        }

        stopForeground(true);
        stopSelf();
    }

    private void createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "VPN Status",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setShowBadge(false);

            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    private void runsocket(ParcelFileDescriptor file){
        try {
            java.net.Socket socket = new java.net.Socket("4.154.154.5", 9999);
            Log.d("TCP_TEST", "Connected to gateway!");

            FileDescriptor fd = file.getFileDescriptor();
            InputStream fileInput = new FileInputStream(fd);
            OutputStream fileOutput = new FileOutputStream(fd);
            InputStream tunnelInput = socket.getInputStream();
            OutputStream tunnelOutput = socket.getOutputStream();


            while (isOn){
                byte[] buffer = new byte[2048];


                int length;

                length = fileInput.read(buffer, 0, 2048);
                if (length > 0) {
                    Log.d("TCP_TEST", "file-to-tunnel Length: " + length);
                    byte lengthHigh = (byte) (length / 256);
                    byte lengthLow = (byte) (length % 256);
                    byte[] newbuffer = new byte[2048+2];
                    newbuffer[0] = lengthHigh;
                    newbuffer[1] = lengthLow;
                    System.arraycopy(buffer, 0, newbuffer, 2, 2048);
                    tunnelOutput.write(newbuffer, 0, length);
                    tunnelOutput.flush();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (int i = 0; i<length-1; i++){
                        sb.append(String.format("%02x", buffer[i]));
                        sb.append(", ");
                    }
                    sb.append(String.format("%02x", buffer[length-1]));
                    sb.append("]");
                    Log.d("TCP_TEST", "Wrote: " + sb);
                    Log.d("TCP_TEST", "file-to-tunnel complete!");
                }
                buffer = new byte[2048];
                byte[] lengthbuffer = new byte[2];

                if (tunnelInput.available() > 2) {
                    length = tunnelInput.read(lengthbuffer, 0, 2);
                    Log.d("TCP_TEST", "read tunnel");
                    if (length > 0) {
                        int packetlength = lengthbuffer[0] * 256 + lengthbuffer[1];
                        Log.d("TCP_TEST", "tunnel-to-file Length: " + length);
                        length = tunnelInput.read(buffer, 0, packetlength);
                        fileOutput.write(buffer, 0, packetlength);
                        fileOutput.flush();
                        Log.d("TCP_TEST", "tunnel-to-file Complete!");
                    }
                }
                Thread.sleep(500);
            }
            socket.close();
        } catch (Exception e) {
            Log.e("TCP_TEST", "Connection failed: " + e.getMessage());
        }


    }
}
