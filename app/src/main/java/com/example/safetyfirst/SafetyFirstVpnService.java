package com.example.safetyfirst;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.IpPrefix;
import android.net.Network;
import android.net.NetworkRequest;
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
import java.net.UnknownHostException;
import java.util.Arrays;
import java.net.InetAddress;


public class SafetyFirstVpnService extends VpnService{

    private static final String TAG = "SafetyFirstVpnService";

    public static final String ACTION_START = "com.example.safetyfirst.action.START";
    public static final String ACTION_STOP = "com.example.safetyfirst.action.STOP";

    private static final String CHANNEL_ID = "safetyfirst_vpn_channel";

    private static final int FOREGROUND_ID = 1;

    private static ConnectivityManager connectivityManager;

    private ParcelFileDescriptor vpnInterface;

    private boolean isOn;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connectivityManager = getSystemService(ConnectivityManager.class);
        if (intent == null || intent.getAction() == null) return START_STICKY;

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand action=" + action);

        if (ACTION_START.equals(action)) {
            isOn = true;
            // Must happen immediately for a foreground service
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
        startForeground(FOREGROUND_ID, notification);
        Log.d(TAG, "startForeground called");
    }
    protected void establishMinimalVpn(){
        try {
            if (vpnInterface != null) return;

            Builder builder = new Builder();

            vpnInterface = builder.setSession("SafetyFirst VPN")
                    .addAddress("10.0.0.2", 32) //bind to an available address in the 192.168.2 range
                    .addRoute("0.0.0.0", 0) //accept all traffic to start
                    .setMtu(1500)
                    .establish();
            if (vpnInterface == null) {
                Log.e(TAG, "Failed to establish VPN. Permission denied or another VPN active.");
            } else {
                Log.d(TAG, "VPN established ");
            }


            new Thread(() -> {
                try  {
                    java.net.Socket socket = new java.net.Socket();
                    socket.setTcpNoDelay(true);
                    socket.setReceiveBufferSize(2048);
                    socket.setSendBufferSize(2048);
                    protect(socket);
                    connectivityManager.getActiveNetwork().bindSocket(socket);
                    setUnderlyingNetworks(new Network[] {connectivityManager.getActiveNetwork()});
                    socket.connect(new java.net.InetSocketAddress("4.154.154.5", 9999));
                    runsocket(vpnInterface.getFileDescriptor(), socket);
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }

            }).start();
        }
        catch (Exception e) {
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

        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();
    }

    private void createChannelIfNeeded() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "VPN Status",
                NotificationManager.IMPORTANCE_LOW
        );
        channel.setShowBadge(false);

        NotificationManager nm = getSystemService(NotificationManager.class);
        if (nm != null) nm.createNotificationChannel(channel);
    }

    private void runsocket(FileDescriptor fd, java.net.Socket socket) {
        try {

            Log.d("TCP_TEST", "Connected to gateway!");

            //create inputs and outputs for the file (vpn interface) and the tunnel (TCP socket to server)
            InputStream fileInput = new FileInputStream(fd);
            OutputStream fileOutput = new FileOutputStream(fd);
            InputStream tunnelInput = socket.getInputStream();
            OutputStream tunnelOutput = socket.getOutputStream();

            byte[] hellobytes = {(byte) 81, (byte) 41 } ;
            tunnelOutput.write(hellobytes);

            //string of a standard ICMP packet pinging 8.8.8.8 for testing the tunnel
            String icmpPacketString = "00 1E 45 00 00 1C 00 01 00 00 40 01 BE BB AC 11 00 04 08 08 08 08 08 00 F7 FF 00 00 00 00".replace(" ", "");
            Log.i("ICMP_TEST", "a1: " + icmpPacketString);
            byte[] icmpPacketBytes = new byte[icmpPacketString.length()/2];
            int index = 0;

            //convert the string into bytes
            while (!icmpPacketString.isEmpty()) {
                String onebytestring = icmpPacketString.substring(0,2);

                icmpPacketBytes[index] = Integer.valueOf(onebytestring, 16).byteValue();
                icmpPacketString = icmpPacketString.substring(2);
                index++;
            }

            for (int i = 0; i<1; i++) {
                Log.i("ICMP_TEST", "e1: " + i);

                Log.i("ICMP_TEST", "result A: " + Arrays.toString(icmpPacketBytes));
                tunnelOutput.write(icmpPacketBytes);
                tunnelOutput.flush();

                Log.i("ICMP_TEST", "e2: " + i);
                byte[] prebuffer = new byte[128];
                tunnelInput.read(prebuffer, 0, 128);
                Log.i("ICMP_TEST", "result B" + Arrays.toString(prebuffer));
                Thread.sleep(1000);
                Log.i("ICMP_TEST", "e3: " + i);

            }
            while (isOn){
                boolean didanything = false;
                byte[] buffer = new byte[2048];
                int length;

                //read packets from file
                length = fileInput.read(buffer, 0, 2048);
                if (length > 0) {
                    didanything = true;
                    Log.d("TCP_TEST", "file-to-tunnel Length: " + length);
                    //turn the length of the packet into a 2 byte prefix
                    byte lengthHigh = (byte) (length / 256);
                    byte lengthLow = (byte) (length % 256);
                    byte[] newbuffer = new byte[2048+2];
                    newbuffer[0] = lengthHigh;
                    newbuffer[1] = lengthLow;

                    //copy the body of the packet into the rest of the buffer
                    System.arraycopy(buffer, 0, newbuffer, 2, length);

                    //write packet
                    tunnelOutput.write(newbuffer, 0, length + 2);
                    tunnelOutput.flush();

                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (int i = 0; i<length-1; i++){
                        sb.append(String.format("%02x", newbuffer[i]));
                        sb.append(", ");
                    }
                    sb.append(String.format("%02x", newbuffer[length-1]));
                    sb.append("]");
                    Log.d("TCP_TEST", "file-to-tunnel Wrote: " + sb);
                }
                //clear buffers
                buffer = new byte[2048];
                byte[] lengthbuffer = new byte[2];

                //check for packets from tunnel
                if (tunnelInput.available() >= 2) {
                    Log.d("TCP_TEST", "tunnel-to-file available: " + tunnelInput.available());
                    didanything = true;

                    //read the 2 byte length prefix
                    length = tunnelInput.read(lengthbuffer, 0, 2);
                    Log.d("TCP_TEST", "read tunnel");
                    if (length > 0) {
                        int packetlength = ((lengthbuffer[0] & 0xFF) << 8) | (lengthbuffer[1] & 0xFF);
                        Log.d("TCP_TEST", "tunnel-to-file Prefix Length: " + length + ", payload length: " + packetlength);

                        //read the body of the payload of length taken from prefix
                        int readlength = tunnelInput.read(buffer, 0, packetlength);

                        //probably should move to helper function for logging
                        StringBuilder sb = new StringBuilder();
                        sb.append("[");
                        for (int i = 0; i<packetlength-1; i++){
                            sb.append(String.format("%02x", buffer[i]));
                            sb.append(", ");
                        }
                        sb.append(String.format("%02x", buffer[packetlength-1]));
                        sb.append("]");

                        Log.d("TCP_TEST", "tunnel-to-file Wrote: " + sb);
                        fileOutput.write(buffer, 0, packetlength);
                        fileOutput.flush();
                        Log.d("TCP_TEST", "tunnel-to-file Complete!");
                    }
                }
                if (!didanything){
                    Thread.sleep(50);
                }
            }


            socket.close();

        } catch (Exception e) {
            Log.e("TCP_TEST", "Connection failed: " + e.getMessage());


        }
    }
}
