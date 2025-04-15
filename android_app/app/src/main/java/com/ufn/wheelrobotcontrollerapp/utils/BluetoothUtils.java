package com.ufn.wheelrobotcontrollerapp.utils;

import android.content.Context;

public class BluetoothUtils {
    public static void saveDeviceMac(Context context, String mac) {
        context.getSharedPreferences("BT_PREFS", Context.MODE_PRIVATE)
                .edit().putString("SELECTED_DEVICE_MAC", mac).apply();
    }

    public static String getSavedDeviceMac(Context context) {
        return context.getSharedPreferences("BT_PREFS", Context.MODE_PRIVATE)
                .getString("SELECTED_DEVICE_MAC", null);
    }
}