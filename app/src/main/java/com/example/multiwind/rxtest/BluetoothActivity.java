package com.example.multiwind.rxtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import java.util.Set;

public class BluetoothActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBluetoothPermissions();
        initBluetooth();
        getDevicesList();

    }

    private void getBluetoothPermissions() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();

    }

    private void initBluetooth() {

    }

    private void getDevicesList() {

    }
}
