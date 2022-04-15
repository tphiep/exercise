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

    private String timestamp;

    public String getDeviceId() {
        return deviceId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public JsonNode getData() {
        return data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public DeviceItem() {
    }

    public DeviceItem(String deviceId, double latitude, double longitude, JsonNode data, String timestamp) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static class Builder {
        private String deviceId;
        private double latitude;
        private double longitude;
        private JsonNode data;
        private String timestamp;

        public Builder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder data(JsonNode data) {
            this.data = data;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DeviceItem build() {
            return new DeviceItem(this.deviceId, this.latitude, this.longitude, this.data, this.timestamp);
        }

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
