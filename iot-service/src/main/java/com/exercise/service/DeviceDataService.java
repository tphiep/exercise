package com.exercise.service;

import com.exercise.domain.Device;
import com.exercise.domain.DeviceData;

public interface DeviceDataService {
    void persist(Device device, DeviceData deviceData, String id);

}
