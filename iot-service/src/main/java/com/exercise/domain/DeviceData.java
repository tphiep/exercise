package com.exercise.domain;


import com.fasterxml.jackson.databind.JsonNode;

public class DeviceData {
    private JsonNode data;

    public DeviceData(JsonNode data) {
        this.data = data;
    }

    public DeviceData() {
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

}
