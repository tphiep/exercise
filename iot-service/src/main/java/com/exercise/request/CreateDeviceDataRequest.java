package com.exercise.request;

import com.exercise.error.ErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeviceDataRequest {

    @NotEmpty(message = "Field is required")
    private String deviceId;

    @NotNull(message = ErrorMessage.REQUIRED)
    @Min(value = -90, message = ErrorMessage.LATITUDE_RANGE)
    @Max(value = 90, message = ErrorMessage.LATITUDE_RANGE)
    private Double latitude;

    @NotNull(message = ErrorMessage.REQUIRED)
    @Min(value = -180, message = ErrorMessage.LONGITUDE_RANGE)
    @Max(value = 180, message = ErrorMessage.LONGITUDE_RANGE)
    private Double longitude;

    @NotNull(message = ErrorMessage.REQUIRED)
    @JsonProperty("data")
    private JsonNode data;
}
