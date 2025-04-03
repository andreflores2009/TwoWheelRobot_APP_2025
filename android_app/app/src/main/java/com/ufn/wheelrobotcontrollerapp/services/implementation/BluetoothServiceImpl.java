package com.ufn.wheelrobotcontrollerapp.services.implementation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.ufn.wheelrobotcontrollerapp.models.Device;
import com.ufn.wheelrobotcontrollerapp.services.BluetoothService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothServiceImpl implements BluetoothService {
    private static final String TAG = "BluetoothService";
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private final Context context;
    private final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Device device;

    public BluetoothServiceImpl(Context context) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    @Override
    public void connect() {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Este dispositivo não suporta Bluetooth");
        }

        if (!bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth está desativado. Ative-o para continuar.");
        }

        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permissão Bluetooth negada!");
        }

        try {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(device.getHc06MACAddress());
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_UUID);
            socket.connect();
            outputStream = socket.getOutputStream();
            Log.d(TAG, "Conectado com sucesso ao dispositivo: " + device.getName());
        } catch (IOException e) {
            Log.e(TAG, "Falha ao conectar ao Bluetooth", e);
        }
    }

    @Override
    public boolean isConnected() {
        return socket != null && socket.isConnected() && outputStream != null;
    }

    @Override
    public void disconnect() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            Log.d(TAG, "Conexão Bluetooth encerrada.");
        } catch (IOException e) {
            Log.e(TAG, "Erro ao fechar conexão Bluetooth", e);
        }
    }

    @Override
    public Device getConnectedDevice() {
        if (!this.isConnected()) {
            return null;
        }
        return device;
    }
}
