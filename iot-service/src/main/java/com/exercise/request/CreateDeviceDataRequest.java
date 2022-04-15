package com.exercise.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateDeviceDataRequest {
    @NotNull
    @NotBlank
    private String deviceId;

    private double latitude;

    private double longitude;

    @JsonProperty("data")
    private JsonNode data;
}
