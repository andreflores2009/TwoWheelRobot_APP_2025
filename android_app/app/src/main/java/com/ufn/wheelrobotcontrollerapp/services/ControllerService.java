package com.ufn.wheelrobotcontrollerapp.services;

import com.ufn.wheelrobotcontrollerapp.models.types.CommandType;

public interface ControllerService {
    void sendCommandToRobot(CommandType commandType);
}
