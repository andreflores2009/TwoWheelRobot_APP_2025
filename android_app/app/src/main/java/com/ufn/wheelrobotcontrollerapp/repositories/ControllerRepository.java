package com.ufn.wheelrobotcontrollerapp.repositories;

import com.ufn.wheelrobotcontrollerapp.models.types.CommandType;

public interface ControllerRepository {
    boolean send(CommandType commandType);
}
