package com.ufn.wheelrobotcontrollerapp.utils;

import androidx.lifecycle.MutableLiveData;

public class BluetoothStatusManager {
    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();

    public BluetoothStatusManager() {
        isConnected.setValue(false);
    }

    public void updateStatus(boolean connected) {
        isConnected.setValue(connected);
    }

    public MutableLiveData<Boolean> getStatus() {
        return isConnected;
    }
}