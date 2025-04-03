package com.ufn.wheelrobotcontrollerapp.services;

public interface BluetoothService {
    boolean connect();
    boolean isConnected();
    void disconnect();
}
