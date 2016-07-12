package com.example.multiwind.rxtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import java.util.Set;

public class BluetoothActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blouetooth_layout);
        getBluetoothPermissions();
        initBluetooth();
        getDevicesList();

    }

    private void getBluetoothPermissions() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null){
            Toast.makeText(this, "Ooops were is bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (!defaultAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){

        }
    }

    private void initBluetooth() {

    }

    private void getDevicesList() {

    }
}
