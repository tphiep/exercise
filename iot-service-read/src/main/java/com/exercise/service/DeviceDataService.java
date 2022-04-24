package com.exercise.service;

import com.exercise.domain.DeviceData;
import com.exercise.domain.Device;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeviceDataService {
    List<DeviceData> findBy(String deviceId, LocalDateTime fromDateTime, LocalDateTime toDateTime);
    Optional<Device> find(String deviceId);
}
