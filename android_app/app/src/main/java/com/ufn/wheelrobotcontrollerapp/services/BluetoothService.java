package com.ufn.wheelrobotcontrollerapp.services;

import com.ufn.wheelrobotcontrollerapp.models.Device;

public interface BluetoothService {
    void connect();
    boolean isConnected();
    void disconnect();
    Device getConnectedDevice();
}
