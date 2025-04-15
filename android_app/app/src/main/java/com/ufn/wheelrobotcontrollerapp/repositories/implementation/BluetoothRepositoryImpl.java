package com.ufn.wheelrobotcontrollerapp.repositories.implementation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.ufn.wheelrobotcontrollerapp.repositories.BluetoothRepository;
import com.ufn.wheelrobotcontrollerapp.services.BluetoothService;
import com.ufn.wheelrobotcontrollerapp.utils.BluetoothStatusManager;
import com.ufn.wheelrobotcontrollerapp.utils.BluetoothUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class BluetoothRepositoryImpl implements BluetoothRepository {
    private static final String TAG = "BluetoothService";
    private final BluetoothStatusManager statusManager;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private final Context context;
    private final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothRepositoryImpl(Context context, BluetoothStatusManager statusManager) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        this.statusManager = statusManager;
    }

    @Override
    public void connect() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth indisponível ou desativado.");
            return;
        }

        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permissão Bluetooth negada!");
            return;
        }

        String savedMac = BluetoothUtils.getSavedDeviceMac(context);
        if (savedMac == null) {
            Log.e(TAG, "Nenhum MAC salvo encontrado.");
            return;
        }

        try {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(savedMac);
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_UUID);
            socket.connect();
            outputStream = socket.getOutputStream();
            Log.d(TAG, "Conectado com sucesso ao dispositivo: " + bluetoothDevice.getName());
            statusManager.updateStatus(true);
        } catch (IOException e) {
            Log.e(TAG, "Erro ao conectar com Bluetooth", e);
            closeSocket();
        }
    }

    @Override
    public boolean isConnected() {
        Boolean status = statusManager.getStatus().getValue();
        return status != null && status;
    }

    @Override
    public void disconnect() {
        try {
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
            Log.d(TAG, "Conexão Bluetooth encerrada.");
            statusManager.updateStatus(false);
        } catch (IOException e) {
            Log.e(TAG, "Erro ao fechar conexão Bluetooth", e);
        } finally {
            socket = null;
            outputStream = null;
        }
    }

    @Override
    public BluetoothDevice getConnectedDevice() {
        if (!isConnected()) return null;
        String mac = BluetoothUtils.getSavedDeviceMac(context);
        return mac != null ? bluetoothAdapter.getRemoteDevice(mac) : null;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public Set<BluetoothDevice> getPairedDevices() {
        if (bluetoothAdapter == null) return Collections.emptySet();

        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permissão BLUETOOTH_CONNECT não concedida.");
            return Collections.emptySet();
        }

        return bluetoothAdapter.getBondedDevices();
    }

    @Override
    public BluetoothDevice getDeviceByMac(String mac) {
        return bluetoothAdapter != null ? bluetoothAdapter.getRemoteDevice(mac) : null;
    }

    @Override
    public void connectToDevice(BluetoothDevice device) {
        if (device == null) return;
        BluetoothUtils.saveDeviceMac(context, device.getAddress());
        connect();
    }

    private void closeSocket() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Erro ao fechar socket", e);
        }
        socket = null;
        outputStream = null;
    }
}