package com.ufn.wheelrobotcontrollerapp.models;

import com.ufn.wheelrobotcontrollerapp.models.types.CommandType;

public class Command {
    private final CommandType type;
    public Command(CommandType type) {
        this.type = type;
    }

    public String toBluetoothMessage() {
        return String.valueOf(type.getCommand());
    }

    public CommandType getType() {
        return type;
    }
}
