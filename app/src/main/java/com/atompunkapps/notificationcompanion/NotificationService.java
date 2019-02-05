package com.atompunkapps.notificationcompanion;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Mahad Ahmed on 1/12/2019.
 */
public class NotificationService extends NotificationListenerService {
    public static UUID MY_UUID = UUID.fromString("ddc9338f-8e5c-4d0b-bddf-306e039ee9e0");
    static String  DEVICE_ADDRESS = "00:18:E4:34:F1:5D";

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    BluetoothSocket socket = null;

    @Override
    public void onCreate() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
    }

    private void sendNotification(String packageName, String title, String message) {
        String tmp[] = packageName.split("\\.");
        if(tmp.length > 0) {
            packageName = tmp[tmp.length-1];
        }
        try {
            socket.getOutputStream().write((packageName+": "+title+"\0"+message+"\0").getBytes());
        }
        catch(IOException e) {
            try {
                socket.close();
            }
            catch(Exception ignored) {}
        }
        System.out.println(title+": "+message);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle bundle = sbn.getNotification().extras;
        System.out.println(bundle);
        ApplicationInfo appInfo = (ApplicationInfo) bundle.get("android.appInfo");
        if(appInfo != null && (appInfo.category == ApplicationInfo.CATEGORY_SOCIAL || appInfo.category ==  ApplicationInfo.CATEGORY_PRODUCTIVITY)) {
            if(socket == null || !socket.isConnected()) {
                if(connect()) {
                    sendNotification(appInfo.packageName, bundle.getString("android.title", ""), bundle.getString("android.text"));
                }
            }
            else {
                sendNotification(appInfo.packageName, bundle.getString("android.title", ""), bundle.getString("android.text"));
            }
        }
    }

    private boolean connect() {
        if(!bluetoothAdapter.isEnabled()) {
            return false;
        }
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothAdapter.cancelDiscovery();
            try {
                socket.connect();
                return true;
            }
            catch(IOException w) {
//                w.printStackTrace();
                try {
                    socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                    socket.connect();
                    return true;
                }
                catch(Exception w2) {
//                    w2.printStackTrace();
                }
            }
        }
        catch(Exception ex) {
//            ex.printStackTrace();
        }
        return false;
    }
}
