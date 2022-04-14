package com.exercise.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

public class DeviceItem {
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("data")
    private JsonNode data;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceItem that = (DeviceItem) o;
        return Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId);
    }
}
