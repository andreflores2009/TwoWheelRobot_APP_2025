package com.ufn.wheelrobotcontrollerapp.services.implementation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.ufn.wheelrobotcontrollerapp.services.BluetoothService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothServiceImpl implements BluetoothService {
    private static final String TAG = "BluetoothService";
    private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String HC06_MAC_ADDRESS = "00:23:10:A0:39:B9";
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private final Context context;

    public BluetoothServiceImpl(Context context) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    @Override
    public boolean connect() {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Este dispositivo não suporta Bluetooth");
            return false;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth está desativado. Ative-o para continuar.");
            return false;
        }

        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permissão Bluetooth negada!");
            return false;
        }

        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(HC06_MAC_ADDRESS);
            socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
            socket.connect();
            outputStream = socket.getOutputStream();
            Log.d(TAG, "Conectado com sucesso ao dispositivo: " + device.getName());
            Toast.makeText(context, "Conectado com sucesso ao dispositivo: " + device.getName(), Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Falha ao conectar ao Bluetooth", e);
            return false;
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
}