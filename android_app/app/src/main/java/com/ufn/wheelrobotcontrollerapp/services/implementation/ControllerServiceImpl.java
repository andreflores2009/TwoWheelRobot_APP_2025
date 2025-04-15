package com.ufn.wheelrobotcontrollerapp.services.implementation;

import com.ufn.wheelrobotcontrollerapp.models.types.CommandType;
import com.ufn.wheelrobotcontrollerapp.repositories.ControllerRepository;
import com.ufn.wheelrobotcontrollerapp.services.ControllerService;

public class ControllerServiceImpl implements ControllerService {
    private final ControllerRepository controllerRepository;

    public ControllerServiceImpl(ControllerRepository controllerRepository) {
        this.controllerRepository = controllerRepository;
    }

    @Override
    public void sendCommandToRobot(CommandType commandType) {
        controllerRepository.send(commandType);
    }
}