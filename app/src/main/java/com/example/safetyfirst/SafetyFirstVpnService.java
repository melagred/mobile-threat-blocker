package com.example.safetyfirst;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


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
            // Must happen immediately for a foreground service
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
        Log.d(TAG, "startForeground called");
    }

    private void establishMinimalVpn() {
        if (vpnInterface != null) return;

        Builder builder = new Builder()
                .setSession("SafetyFirst VPN")
                .addAddress("10.0.0.0", 32); // valid host IP
                //.addRoute("0.0.0.0", 0);    // route all traffic

        vpnInterface = builder.establish();
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
            java.net.Socket socket = new java.net.Socket("20.3.103.96", 9999);
            Log.d("TCP_TEST", "Connected to gateway!");

            FileDescriptor fd = file.getFileDescriptor();
            InputStream fileInput = new FileInputStream(fd);
            OutputStream fileOutput = new FileOutputStream(fd);
            InputStream tunnelInput = socket.getInputStream();
            OutputStream tunnelOutput = socket.getOutputStream();
            sendMessage("VPN connected to gateway");





            while (isOn){
                byte[] buffer = new byte[2048];

                int length;
                length = fileInput.read(buffer, 0, 2);
                if (length > 0){
                    sendMessage("packet sent: " + length);
                    byte lengthHigh = (byte) (length / 256);
                    byte lengthLow = (byte) (length % 256);
                    tunnelOutput.write(new byte[]{lengthHigh, lengthLow});
                    tunnelOutput.write(buffer);

                }
                Thread.sleep(5);
                buffer = new byte[2048];
                byte[] lengthbuffer = new byte[2];
                length = tunnelInput.read(lengthbuffer, 0, 2);
                if (length > 0) {
                    int packetlength = lengthbuffer[0] * 256 + lengthbuffer[1];
                    sendMessage("Packet recieved: " + packetlength);
                    length = tunnelInput.read(buffer, 0, packetlength);
                    fileOutput.write(buffer);
                }

            }
            socket.close();
        } catch (Exception e) {
            Log.e("TCP_TEST", "Connection failed: " + e.getMessage());
            sendMessage("ERROR: " + e.getMessage());
        }


    }
}


// public class SafetyFirstVpnService extends VpnService{

//     private static final String TAG = "SafetyFirstVpnService";

//     public static final String ACTION_START = "com.example.safetyfirst.action.START";
//     public static final String ACTION_STOP = "com.example.safetyfirst.action.STOP";

//     private static final String CHANNEL_ID = "safetyfirst_vpn_channel";

//     private static final int FOREGROUND_ID = 1;

//     private static ConnectivityManager connectivityManager;

//     private ParcelFileDescriptor vpnInterface;

//     private boolean isOn;

//     @Override
//     public int onStartCommand(Intent intent, int flags, int startId) {
//         connectivityManager = getSystemService(ConnectivityManager.class);
//         if (intent == null || intent.getAction() == null) return START_STICKY;

//         String action = intent.getAction();
//         Log.d(TAG, "onStartCommand action=" + action);

//         if (ACTION_START.equals(action)) {
//             isOn = true;
//             // Must happen immediately for a foreground service
//             updateForegroundNotification("Protection is ON");
//             Log.d("SafetyFirstVpnService", "establishing");
//             establishMinimalVpn();
//         } else if (ACTION_STOP.equals(action)) {
//             isOn = false;
//             stopVpn();
//         }

//         return START_STICKY;
//     }

//     private void updateForegroundNotification(String text) {
//         createChannelIfNeeded();

//         Intent openApp = new Intent(this, MainActivity.class);
//         openApp.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//         PendingIntent pi = PendingIntent.getActivity(
//                 this,
//                 0,
//                 openApp,
//                 PendingIntent.FLAG_IMMUTABLE
//         );

//         Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                 .setSmallIcon(android.R.drawable.ic_lock_lock)
//                 .setContentTitle("Safety First")
//                 .setContentText(text)
//                 .setOngoing(true)
//                 .setAutoCancel(false)
//                 .setCategory(NotificationCompat.CATEGORY_SERVICE)
//                 .setContentIntent(pi)
//                 .build();
//         startForeground(FOREGROUND_ID, notification);
//         Log.d(TAG, "startForeground called");
//     }
//     protected void establishMinimalVpn(){
//         try {
//             if (vpnInterface != null) return;

//             Builder builder = new Builder();

//             vpnInterface = builder.setSession("SafetyFirst VPN")
//                     .addAddress("10.0.0.2", 32) //bind to an available address in the 192.168.2 range
//                     .addRoute("0.0.0.0", 0) //accept all traffic to start
//                     .setMtu(1500)
//                     .establish();
//             if (vpnInterface == null) {
//                 Log.e(TAG, "Failed to establish VPN. Permission denied or another VPN active.");
//             } else {
//                 Log.d(TAG, "VPN established ");
//             }


//             new Thread(() -> {
//                 try  {
//                     java.net.Socket socket = new java.net.Socket();
//                     socket.setTcpNoDelay(true);
//                     socket.setReceiveBufferSize(2048);
//                     socket.setSendBufferSize(2048);
//                     protect(socket);
//                     connectivityManager.getActiveNetwork().bindSocket(socket);
//                     setUnderlyingNetworks(new Network[] {connectivityManager.getActiveNetwork()});
//                     socket.connect(new java.net.InetSocketAddress("20.3.103.96", 9999));
//                     runsocket(vpnInterface.getFileDescriptor(), socket);
//                 } catch (java.io.IOException e) {
//                     throw new RuntimeException(e);
//                 }

//             }).start();
//         }
//         catch (Exception e) {
//             Log.e(TAG, "Failed to establish VPN or start socket thread", e);
//         }
//     }

//     private void stopVpn() {
//         Log.d(TAG, "Stopping VPN");
//         try {
//             if (vpnInterface != null) {
//                 vpnInterface.close();
//                 vpnInterface = null;
//             }
//         } catch (Exception e) {
//             Log.e(TAG, "Error closing vpnInterface", e);
//         }

//         stopForeground(STOP_FOREGROUND_REMOVE);
//         stopSelf();
//     }

//     private void createChannelIfNeeded() {
//         NotificationChannel channel = new NotificationChannel(
//                 CHANNEL_ID,
//                 "VPN Status",
//                 NotificationManager.IMPORTANCE_LOW
//         );
//         channel.setShowBadge(false);

//         NotificationManager nm = getSystemService(NotificationManager.class);
//         if (nm != null) nm.createNotificationChannel(channel);
//     }

//     private void runsocket(FileDescriptor fd, java.net.Socket socket) {
//         try {

//             Log.d("TCP_TEST", "Connected to gateway!");

//             //create inputs and outputs for the file (vpn interface) and the tunnel (TCP socket to server)
//             InputStream fileInput = new FileInputStream(fd);
//             OutputStream fileOutput = new FileOutputStream(fd);
//             InputStream tunnelInput = socket.getInputStream();
//             OutputStream tunnelOutput = socket.getOutputStream();

//             byte[] hellobytes = {(byte) 81, (byte) 41 } ;
//             tunnelOutput.write(hellobytes);

//             //string of a standard ICMP packet pinging 8.8.8.8 for testing the tunnel
//             String icmpPacketString = "00 1E 45 00 00 1C 00 01 00 00 40 01 BE BB C0 C6 02 00 08 08 08 08 08 00 F7 FF 00 00 00 00".replace(" ", "");
//             //Log.i("ICMP_TEST", "a1: " + icmpPacketString);
//             byte[] icmpPacketBytes = new byte[icmpPacketString.length()/2];
//             int index = 0;

//             //convert the string into bytes
//             while (!icmpPacketString.isEmpty()) {
//                 String onebytestring = icmpPacketString.substring(0,2);

//                 icmpPacketBytes[index] = Integer.valueOf(onebytestring, 16).byteValue();
//                 icmpPacketString = icmpPacketString.substring(2);
//                 index++;
//             }

//             for (int i = 0; i<1; i++) {
//                 //Log.i("ICMP_TEST", "e1: " + i);

//                 Log.i("ICMP_TEST", "Sending echo request: " + Arrays.toString(icmpPacketBytes));
//                 tunnelOutput.write(icmpPacketBytes);
//                 tunnelOutput.flush();

//                 //Log.i("ICMP_TEST", "e2: " + i);
//                 byte[] prebuffer = new byte[128];
//                 tunnelInput.read(prebuffer, 0, 128);
//                 Log.i("ICMP_TEST", "Received echo reply: " + Arrays.toString(prebuffer));
//                 Thread.sleep(1000);
//                 //Log.i("ICMP_TEST", "e3: " + i);

//             }
//             byte[] buffer = new byte[2048];
//             byte[] newbuffer = new byte[2048+2];
//             byte[] lengthbuffer = new byte[2];
//             while (isOn){
//                 boolean didanything = false;
//                 Arrays.fill(buffer, (byte) 0);
//                 int length;

//                 //read packets from file
//                 length = fileInput.read(buffer, 0, 2048);
//                 if (length > 0) {
//                     didanything = true;
//                     //Log.d("TCP_TEST", "file-to-tunnel Length: " + length);
//                     //turn the length of the packet into a 2 byte prefix
//                     byte lengthHigh = (byte) (length / 256);
//                     byte lengthLow = (byte) (length % 256);
//                     newbuffer[0] = lengthHigh;
//                     newbuffer[1] = lengthLow;

//                     //copy the body of the packet into the rest of the buffer
//                     System.arraycopy(buffer, 0, newbuffer, 2, length);

//                     //write packet
//                     tunnelOutput.write(newbuffer, 0, length + 2);
//                     tunnelOutput.flush();

//                     /*
//                     StringBuilder sb = new StringBuilder();
//                     sb.append("[");
//                     for (int i = 0; i<length-1; i++){
//                         sb.append(String.format("%02x", newbuffer[i]));
//                         sb.append(", ");
//                     }
//                     sb.append(String.format("%02x", newbuffer[length-1]));
//                     sb.append("]");
//                     Log.d("TCP_TEST", "file-to-tunnel Wrote: " + sb);

//                      */
//                 }
//                 //clear buffers


//                 //check for packets from tunnel
//                 if (tunnelInput.available() >= 2) {
//                     Log.d("TCP_TEST", "tunnel-to-file available: " + tunnelInput.available());
//                     didanything = true;

//                     //read the 2 byte length prefix
//                     length = tunnelInput.read(lengthbuffer, 0, 2);
//                     Log.d("TCP_TEST", "read tunnel");
//                     if (length > 0) {
//                         int packetlength = ((lengthbuffer[0] & 0xFF) << 8) | (lengthbuffer[1] & 0xFF);
//                         //Log.d("TCP_TEST", "tunnel-to-file Prefix Length: " + length + ", payload length: " + packetlength);

//                         //read the body of the payload of length taken from prefix
//                         int readlength = tunnelInput.read(buffer, 0, packetlength);

//                         //probably should move to helper function for logging
//                         /*
//                         StringBuilder sb = new StringBuilder();
//                         sb.append("[");
//                         for (int i = 0; i<packetlength-1; i++){
//                             sb.append(String.format("%02x", buffer[i]));
//                             sb.append(", ");
//                         }
//                         sb.append(String.format("%02x", buffer[packetlength-1]));
//                         sb.append("]");
//                         Log.d("TCP_TEST", "tunnel-to-file Wrote: " + sb);
//                         */
//                         fileOutput.write(buffer, 0, readlength);
//                         fileOutput.flush();
//                         //Log.d("TCP_TEST", "tunnel-to-file Complete!");
//                     }
//                 }
//                 if (!didanything){
//                     Thread.sleep(50);
//                 }
//             }


//             socket.close();

//         } catch (Exception e) {
//             Log.e("TCP_TEST", "Connection failed: " + e.getMessage());


//         }
//     }
// }
