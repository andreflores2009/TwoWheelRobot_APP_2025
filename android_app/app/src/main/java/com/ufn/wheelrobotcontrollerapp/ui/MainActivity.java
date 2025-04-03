package com.ufn.wheelrobotcontrollerapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import com.ufn.wheelrobotcontrollerapp.services.BluetoothService;
import com.ufn.wheelrobotcontrollerapp.services.implementation.BluetoothServiceImpl;

public class MainActivity extends AppCompatActivity {
    private BluetoothService bluetoothService;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bluetoothService = new BluetoothServiceImpl(this);
        super.onCreate(savedInstanceState);
        checkBluetoothPermissions();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bluetoothService.connect();
            } else {
                Log.e("Bluetooth", "Permissão negada pelo usuário.");
                Toast.makeText(this, "Permissão de Bluetooth necessária!", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("Permissão Necessária")
                        .setMessage("O aplicativo precisa de acesso ao Bluetooth. Vá para as Configurações e ative a permissão.")
                        .setPositiveButton("Abrir Configurações", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Fechar", (dialog, which) -> finish())
                        .show();
            }
        }
    }


}