package com.exercise.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceDataResult {
    private String deviceId;
    private Double longitude;
    private Double latitude;
    List<Map<String, Object>> data = new ArrayList<>();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void addData(Map<String, Object> deviceData) {
        this.data.add(deviceData);
    }

    public List<Map<String, Object>> getData() {
        return data;
    }
}
