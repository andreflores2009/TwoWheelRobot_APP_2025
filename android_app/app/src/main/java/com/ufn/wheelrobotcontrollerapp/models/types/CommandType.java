package com.ufn.wheelrobotcontrollerapp.models.types;

public enum CommandType {
    FORWARD('F'),
    BACKWARD('B'),
    LEFT('L'),
    RIGHT('R'),
    CROSS('X'),
    SQUARE('S'),
    PAUSE('P'),
    TRIANGLE('T'),
    CIRCLE('C');

    private final char command;

    CommandType(char command) {
        this.command = command;
    }

    public char getCommand() {
        return command;
    }
}
