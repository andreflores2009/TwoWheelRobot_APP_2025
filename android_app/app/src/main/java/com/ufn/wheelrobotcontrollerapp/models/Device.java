package com.ufn.wheelrobotcontrollerapp.models;

import java.util.UUID;

public class Device {
    private String name;
    public final String hc06MACAddress = "00:23:10:A0:39:B9";

    public void setName(String name) {
        name = name;
    }

    public String getName() {
        return name;
    }

    public String getHc06MACAddress() {
        return hc06MACAddress;
    }
}
