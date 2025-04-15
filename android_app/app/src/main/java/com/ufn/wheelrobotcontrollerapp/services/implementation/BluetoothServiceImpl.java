package com.ufn.wheelrobotcontrollerapp.services.implementation;

import android.bluetooth.BluetoothDevice;
import com.ufn.wheelrobotcontrollerapp.repositories.BluetoothRepository;
import com.ufn.wheelrobotcontrollerapp.services.BluetoothService;
import java.io.OutputStream;
import java.util.Set;

public class BluetoothServiceImpl implements BluetoothService {
    private final BluetoothRepository bluetoothRepository;

    public BluetoothServiceImpl(BluetoothRepository bluetoothRepository) {
        this.bluetoothRepository = bluetoothRepository;
    }

    @Override
    public void connect() {
        bluetoothRepository.connect();
    }

    @Override
    public void connectToDevice(BluetoothDevice device) {
        bluetoothRepository.connectToDevice(device);
    }

    @Override
    public void disconnect() {
        bluetoothRepository.disconnect();
    }

    @Override
    public boolean isConnected() {
        return bluetoothRepository.isConnected();
    }

    @Override
    public BluetoothDevice getConnectedDevice() {
        return bluetoothRepository.getConnectedDevice();
    }

    @Override
    public OutputStream getOutputStream() {
        return bluetoothRepository.getOutputStream();
    }

    @Override
    public Set<BluetoothDevice> getPairedDevices() {
        return bluetoothRepository.getPairedDevices();
    }

    @Override
    public BluetoothDevice getDeviceByMac(String mac) {
        return bluetoothRepository.getDeviceByMac(mac);
    }
}