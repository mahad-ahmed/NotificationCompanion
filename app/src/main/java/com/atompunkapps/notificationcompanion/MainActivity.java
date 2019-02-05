package com.atompunkapps.notificationcompanion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//
//        registerReceiver(NotificationService.broadcastReceiver, intentFilter);
    }
}
