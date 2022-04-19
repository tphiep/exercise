package com.exercise.service;

import com.exercise.exception.DeviceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface DeviceDataService {
    String find(String deviceId, String from, String to) throws DeviceNotFoundException, JsonProcessingException;
}
