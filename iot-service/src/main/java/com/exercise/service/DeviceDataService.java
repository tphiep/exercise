package com.exercise.service;

import com.exercise.domain.Device;

public interface DeviceDataService {
    void persist(Device device, String json, String id);
}
