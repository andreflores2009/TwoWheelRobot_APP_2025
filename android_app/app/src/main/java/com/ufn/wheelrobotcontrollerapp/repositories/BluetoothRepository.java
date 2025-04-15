package com.ufn.wheelrobotcontrollerapp.repositories;

import android.bluetooth.BluetoothDevice;

import java.io.OutputStream;
import java.util.Set;

public interface BluetoothRepository {
    void connect();
    void connectToDevice(BluetoothDevice device);
    void disconnect();
    boolean isConnected();
    BluetoothDevice getConnectedDevice();
    OutputStream getOutputStream();
    Set<BluetoothDevice> getPairedDevices();
    BluetoothDevice getDeviceByMac(String mac);
}