package com.example.multiwind.rxtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1000;
    private static final UUID CREATED_UUID = UUID.randomUUID();
    private static final String NAME = BluetoothActivity.class.getName();
    public BroadcastReceiver bluetoothStateReceiver = new BluetoothStateBroadcastReceiver();
    private BroadcastReceiver bluetoothFoundReceiver;
    private BroadcastReceiver bluetoothStartFinishDiscoveryReceiver;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blouetooth_layout);

        bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter != null) {
            initReceivers();
            makeDeviceDiscoverable();
            boolean isEnabled = enableBluetooth(bluetoothAdapter);
            if (isEnabled) {
                retrieveBluetoothDevices();
            }
        }
    }

    private void makeDeviceDiscoverable() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);
    }

    private void retrieveBluetoothDevices() {
        retrievePairedDevices(bluetoothAdapter);
        bluetoothAdapter.startDiscovery();
        try {
            bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, CREATED_UUID);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Can't establish bluetooth listeninig", Toast.LENGTH_LONG).show();
        }
    }

    private void initReceivers() {
        bluetoothFoundReceiver = new BluetoothFoundBroadcastReceiver(bluetoothAdapter);
        bluetoothStateReceiver = new BluetoothStateBroadcastReceiver();
        bluetoothStartFinishDiscoveryReceiver = new BluetoothStartFinishDiscoveryReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bluetoothStateReceiver);
        unregisterReceiver(bluetoothFoundReceiver);
        unregisterReceiver(bluetoothStartFinishDiscoveryReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBluetoothReceivers();
    }

    private void retrievePairedDevices(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                Log.d(BluetoothActivity.class.getSimpleName(), device.toString());
            }
        }
    }

    private void initBluetoothReceivers() {
        registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(bluetoothFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(bluetoothStartFinishDiscoveryReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(bluetoothStartFinishDiscoveryReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }

    private BluetoothAdapter getBluetoothAdapter() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            Toast.makeText(this, "Ooops were is bluetooth", Toast.LENGTH_LONG).show();
        }
        return defaultAdapter;
    }

    private boolean enableBluetooth(BluetoothAdapter defaultAdapter) {
        if (!defaultAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
            Toast.makeText(this, "Bluetooth was enabled", Toast.LENGTH_LONG).show();
            retrieveBluetoothDevices();
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_ENABLE_BT) {
            Toast.makeText(this, "Bluetooth was NOT enabled", Toast.LENGTH_LONG).show();
        }
    }

    private static class BluetoothStateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int extraState = (int) extras.get(BluetoothAdapter.EXTRA_STATE);
                int extraPreviouseState = (int) extras.get(BluetoothAdapter.EXTRA_PREVIOUS_STATE);
                Log.d(BluetoothActivity.class.getSimpleName(), "State of bluetooth was changed from " + extraState + " to " + extraPreviouseState);
            }
        }
    }

    private static class BluetoothFoundBroadcastReceiver extends BroadcastReceiver {

        private BluetoothAdapter bluetoothAdapter;

        public BluetoothFoundBroadcastReceiver(BluetoothAdapter bluetoothAdapter) {
            this.bluetoothAdapter = bluetoothAdapter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND == intent.getAction()) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String bluetoothName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                Log.d(BluetoothActivity.class.getSimpleName(), bluetoothDevice + " " + bluetoothName);

                if (bluetoothAdapter != null) {
                    bluetoothAdapter.cancelDiscovery();
                }
            }
        }
    }

    private class BluetoothStartFinishDiscoveryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == intent.getAction()) {
                Log.d(BluetoothActivity.class.getSimpleName(), "State searching");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == intent.getAction()) {
                Log.d(BluetoothActivity.class.getSimpleName(), "State stop searching");
            }
        }
    }

    static class AcceptTread extends Thread {
        private AcceptanceCallback acceptanceCallback;
        private BluetoothServerSocket serverSocket;

        public AcceptTread(AcceptanceCallback acceptanceCallback, BluetoothAdapter bluetoothAdapter) {
            this.acceptanceCallback = acceptanceCallback;
            BluetoothServerSocket tmpSocket = null;
            if (bluetoothAdapter != null) {
                try {
                    tmpSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, CREATED_UUID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = tmpSocket;
            }

        }

        @Override
        public void run() {
            super.run();
            BluetoothSocket bluetoothSocket;
            while (true) {
                try {
                    bluetoothSocket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    cancel();
                    break;
                }

                if (bluetoothSocket != null) {
                    acceptanceCallback.accepted(bluetoothSocket);
                    cancel();
                }
            }
        }

        private void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private interface AcceptanceCallback {
            void accepted(BluetoothSocket acceptedSocket);

            void rejected();
        }
    }
}