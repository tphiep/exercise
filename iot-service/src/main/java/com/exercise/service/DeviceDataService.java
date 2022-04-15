package com.exercise.service;

public interface DeviceDataService {
    void persist(String doc, String id);
    String find(String deviceId, String from, String to);
}
