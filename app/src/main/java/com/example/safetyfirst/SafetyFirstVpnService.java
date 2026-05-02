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
import java.io.InputStream;
import java.io.OutputStream;


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
        Log.d("VPN_DEBUG", "VPN Service started");
        sendMessage("DEBUG: Service started");

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand action=" + action);

        if (ACTION_START.equals(action)) {
            isOn = true;
            updateForegroundNotification("Protection is ON");
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
        openApp.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                openApp,
                PendingIntent.FLAG_IMMUTABLE
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

        if (Build.VERSION.SDK_INT >= 29) {
            startForeground(
                    FOREGROUND_ID,
                    notification,
                    android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            );
        } else {
            startForeground(FOREGROUND_ID, notification);
        }
        Log.d(TAG, "startForeground called");
    }

    private void establishMinimalVpn() {
        if (vpnInterface != null) return;
        try {
            vpnInterface = new Builder()
                    .setSession("SafetyFirst VPN")
                    .addAddress("10.0.0.2", 32)
                    .addRoute("0.0.0.0", 0)
                    .addDnsServer("8.8.8.8")
                    .setMtu(1500)
                    .establish();
            if (vpnInterface == null) {
                Log.e(TAG, "Failed to establish VPN. Permission denied or another VPN active.");
                return;
            }
            Log.d(TAG, "VPN established");
            new Thread(() -> runsocket(vpnInterface)).start();
        } catch (Exception e) {
            Log.e(TAG, "Failed to establish VPN or start socket thread", e);
        }
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

    private void sendMessage(String msg) {
        Intent intent = new Intent("VPN_NOTIF");
        intent.setPackage(getPackageName());
        intent.putExtra("msg", msg);
        sendBroadcast(intent);
    }

    private void runsocket(ParcelFileDescriptor file) {
        Log.d("VPN_DEBUG", "runsocket started");
        sendMessage("DEBUG: runsocket started");
        try {
            java.net.Socket socket = new java.net.Socket();
            socket.setTcpNoDelay(true);
            protect(socket);
            socket.connect(new java.net.InetSocketAddress("20.3.103.96", 9999));
            Log.d("TCP_TEST", "Connected to gateway!");

            FileDescriptor fd = file.getFileDescriptor();
            InputStream fileInput = new FileInputStream(fd);
            OutputStream fileOutput = new FileOutputStream(fd);
            InputStream tunnelInput = socket.getInputStream();
            OutputStream tunnelOutput = socket.getOutputStream();
            sendMessage("VPN connected to gateway");

            byte[] hellobytes = {(byte) 0x51, (byte) 0x29};
            tunnelOutput.write(hellobytes);
            tunnelOutput.flush();

            // Outbound: TUN -> tunnel. Reads block on TUN until a packet is ready.
            Thread outbound = new Thread(() -> {
                try {
                    byte[] buffer = new byte[32768];
                    while (isOn) {
                        int length = fileInput.read(buffer);
                        if (length <= 0) continue;
                        byte[] packet = new byte[length + 2];
                        packet[0] = (byte) (length >> 8);
                        packet[1] = (byte) (length & 0xFF);
                        System.arraycopy(buffer, 0, packet, 2, length);
                        tunnelOutput.write(packet);
                        tunnelOutput.flush();
                    }
                } catch (Exception e) {
                    Log.e("TCP_TEST", "Outbound error: " + e.getMessage());
                    isOn = false;
                }
            });

            // Inbound: tunnel -> TUN. Reads block on the socket until bytes arrive.
            Thread inbound = new Thread(() -> {
                try {
                    byte[] lengthbuf = new byte[2];
                    byte[] buffer = new byte[65535];
                    while (isOn) {
                        int total = 0, r;
                        while (total < 2) {
                            r = tunnelInput.read(lengthbuf, total, 2 - total);
                            if (r < 0) { isOn = false; return; }
                            total += r;
                        }
                        int packetlength = ((lengthbuf[0] & 0xFF) << 8) | (lengthbuf[1] & 0xFF);
                        if (packetlength <= 0 || packetlength > buffer.length) {
                            Log.e("TCP_TEST", "Bad inbound length: " + packetlength);
                            isOn = false;
                            return;
                        }
                        total = 0;
                        while (total < packetlength) {
                            r = tunnelInput.read(buffer, total, packetlength - total);
                            if (r < 0) { isOn = false; return; }
                            total += r;
                        }
                        fileOutput.write(buffer, 0, packetlength);
                        fileOutput.flush();
                    }
                } catch (Exception e) {
                    Log.e("TCP_TEST", "Inbound error: " + e.getMessage());
                    isOn = false;
                }
            });

            outbound.start();
            inbound.start();
            outbound.join();
            inbound.join();
            socket.close();
        } catch (Exception e) {
            Log.e("TCP_TEST", "Connection failed: " + e.getMessage());
            sendMessage("ERROR: " + e.getMessage());
        }
    }
}
