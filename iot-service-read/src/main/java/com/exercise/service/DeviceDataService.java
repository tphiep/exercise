package com.exercise.service;

import com.exercise.domain.DeviceData;
import com.exercise.domain.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceDataService {
//    String find(String deviceId, String from, String to) throws DeviceNotFoundException, JsonProcessingException;
    List<DeviceData> findBy(String deviceId, String fromDateTime, String toDateTime);
    Optional<Device> find(String deviceId);
}
