package com.exercise.service;

import com.exercise.domain.DeviceItem;

import java.time.LocalDateTime;

public interface DeviceDataService {
    void persist(DeviceItem item);
    void persist(String doc, String id);
    void find(String deviceId, LocalDateTime from, LocalDateTime to);
}
