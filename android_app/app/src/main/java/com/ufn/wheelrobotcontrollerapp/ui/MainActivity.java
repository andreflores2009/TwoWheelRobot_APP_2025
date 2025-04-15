package com.ufn.wheelrobotcontrollerapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ufn.wheelrobotcontrollerapp.R;
import com.ufn.wheelrobotcontrollerapp.models.types.CommandType;
import com.ufn.wheelrobotcontrollerapp.repositories.BluetoothRepository;
import com.ufn.wheelrobotcontrollerapp.repositories.ControllerRepository;
import com.ufn.wheelrobotcontrollerapp.repositories.implementation.BluetoothRepositoryImpl;
import com.ufn.wheelrobotcontrollerapp.repositories.implementation.ControllerRepositoryImpl;
import com.ufn.wheelrobotcontrollerapp.services.BluetoothService;
import com.ufn.wheelrobotcontrollerapp.services.ControllerService;
import com.ufn.wheelrobotcontrollerapp.services.implementation.BluetoothServiceImpl;
import com.ufn.wheelrobotcontrollerapp.services.implementation.ControllerServiceImpl;
import com.ufn.wheelrobotcontrollerapp.utils.BluetoothStatusManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private static final int REPEAT_INTERVAL_MS = 100;
    private ControllerService controllerService;
    private BluetoothService bluetoothService;
    private final Handler repeatHandler = new Handler(Looper.getMainLooper());
    private Runnable repeatRunnable;
    private boolean isSending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeServicesAndRepositories();
        checkBluetoothPermissions();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        applyWindowInsets();

        setupControlButtons();
        setupBluetoothControls();
    }

    private void initializeServicesAndRepositories() {
        BluetoothRepository bluetoothRepository = new BluetoothRepositoryImpl(this, new BluetoothStatusManager());
        bluetoothService = new BluetoothServiceImpl(bluetoothRepository);
        ControllerRepository controllerRepository = new ControllerRepositoryImpl(bluetoothService);
        controllerService = new ControllerServiceImpl(controllerRepository);
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupControlButtons() {
        bindRepeatingButton(R.id.btnCima, CommandType.BACKWARD);
        bindRepeatingButton(R.id.btnBaixo, CommandType.FORWARD);
        bindRepeatingButton(R.id.btnEsquerda, CommandType.LEFT);
        bindRepeatingButton(R.id.btnDireita, CommandType.RIGHT);
        bindRepeatingButton(R.id.btnAcelera, CommandType.CROSS);
        bindRepeatingButton(R.id.btnReduz, CommandType.SQUARE);
        bindRepeatingButton(R.id.btnMaisCurva, CommandType.CIRCLE);
        bindRepeatingButton(R.id.btnMenosCurva, CommandType.TRIANGLE);
        bindRepeatingButton(R.id.btnParar, CommandType.PAUSE);
    }

    private void setupBluetoothControls() {
        Button btnConnect = findViewById(R.id.btnConectar);
        Button btnDisconnect = findViewById(R.id.btnDesconectar);
        TextView connectionStatus = findViewById(R.id.statusConexao);

        btnDisconnect.setVisibility(View.INVISIBLE);

        btnConnect.setOnClickListener(v -> showBluetoothDevicesDialog(connectionStatus, btnConnect, btnDisconnect));
        btnDisconnect.setOnClickListener(v -> handleDisconnect(btnConnect, btnDisconnect, connectionStatus));
    }

    private void bindRepeatingButton(int buttonId, CommandType command) {
        Button button = findViewById(buttonId);
        button.setOnTouchListener((v, event) -> handleTouchEvent(event, command));
    }

    private boolean handleTouchEvent(MotionEvent event, CommandType commandType) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startRepeatingCommand(commandType);
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopRepeatingCommand(event.getAction() == MotionEvent.ACTION_CANCEL);
                return true;

            default:
                return false;
        }
    }

    private void startRepeatingCommand(CommandType commandType) {
        if (isSending) return;

        isSending = true;
        repeatRunnable = new Runnable() {
            @Override
            public void run() {
                if (isSending) {
                    sendCommand(commandType);
                    repeatHandler.postDelayed(this, REPEAT_INTERVAL_MS);
                }
            }
        };
        repeatHandler.post(repeatRunnable);
    }

    private void stopRepeatingCommand(boolean isCancel) {
        isSending = false;
        repeatHandler.removeCallbacks(repeatRunnable);
        if (isCancel) sendCommand(CommandType.SQUARE);
    }

    private void sendCommand(CommandType commandType) {
        if (bluetoothService.isConnected()) {
            controllerService.sendCommandToRobot(commandType);
        } else {
            bluetoothService.disconnect();
            Toast.makeText(this, "Bluetooth não está conectado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDisconnect(Button btnConnect, Button btnDisconnect, TextView statusLabel) {
        bluetoothService.disconnect();
        btnConnect.setVisibility(View.VISIBLE);
        btnDisconnect.setVisibility(View.INVISIBLE);
        statusLabel.setText("Status: aguardando conexão...");
    }

    private void showBluetoothDevicesDialog(TextView label, Button btnConnect, Button btnDisconnect) {
        checkBluetoothPermissions();
        Set<BluetoothDevice> pairedDevices = bluetoothService.getPairedDevices();

        if (pairedDevices.isEmpty()) {
            Toast.makeText(this, "Nenhum dispositivo pareado encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<BluetoothDevice> deviceList = new ArrayList<>();
        List<String> deviceNames = new ArrayList<>();

        for (BluetoothDevice device : pairedDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                requestBluetoothPermissionDialog();
                return;
            }
            deviceList.add(device);
            deviceNames.add(device.getName() + " - " + device.getAddress());
        }

        new AlertDialog.Builder(this)
                .setTitle("Selecione um dispositivo pareado")
                .setItems(deviceNames.toArray(new String[0]), (dialog, which) -> {
                    BluetoothDevice selectedDevice = deviceList.get(which);
                    ((BluetoothServiceImpl) bluetoothService).connectToDevice(selectedDevice);
                    label.setText("Conectado ao aparelho: " + selectedDevice.getName());
                    btnConnect.setVisibility(View.INVISIBLE);
                    btnDisconnect.setVisibility(View.VISIBLE);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void checkBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSION);
        }
    }

    private void requestBluetoothPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permissão Necessária")
                .setMessage("O aplicativo precisa de acesso ao Bluetooth. Vá para as configurações e ative a permissão.")
                .setPositiveButton("Abrir Configurações", (dialog, which) -> openAppSettings())
                .setNegativeButton("Fechar", (dialog, which) -> finish())
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bluetoothService.connect();
            } else {
                requestBluetoothPermissionDialog();
            }
        }
    }
}