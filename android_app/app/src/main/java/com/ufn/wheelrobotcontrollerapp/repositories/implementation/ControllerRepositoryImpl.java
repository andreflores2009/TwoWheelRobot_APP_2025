package com.ufn.wheelrobotcontrollerapp.repositories.implementation;

import android.util.Log;

import com.ufn.wheelrobotcontrollerapp.models.Command;
import com.ufn.wheelrobotcontrollerapp.models.types.CommandType;
import com.ufn.wheelrobotcontrollerapp.repositories.ControllerRepository;
import com.ufn.wheelrobotcontrollerapp.services.BluetoothService;

import java.io.IOException;
import java.io.OutputStream;

public class ControllerRepositoryImpl implements ControllerRepository {
    private static final String TAG = "ControllerRepository";
    private final BluetoothService bluetoothService;

    public ControllerRepositoryImpl(BluetoothService bluetoothService) {
        this.bluetoothService = bluetoothService;
    }

    public boolean send(CommandType commandType) {
        if (!bluetoothService.isConnected()) {
            Log.e(TAG, "Bluetooth não está conectado. Não é possível enviar comando.");
            return false;
        }

        Command command = new Command(commandType);
        String message = command.toBluetoothMessage();

        try {
            OutputStream outputStream = bluetoothService.getOutputStream();
            outputStream.write(message.getBytes());
            outputStream.flush();
            Log.d(TAG, "Comando enviado ao robô: " + message);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Erro ao enviar comando ao robô: " + message, e);
            return false;
        }
    }
}