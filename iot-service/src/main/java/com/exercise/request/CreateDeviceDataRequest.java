package com.exercise.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeviceDataRequest {
    @NotNull
    @NotBlank
    private String deviceId;

    private double latitude;

    private double longitude;

    @JsonProperty("data")
    private JsonNode data;
}
