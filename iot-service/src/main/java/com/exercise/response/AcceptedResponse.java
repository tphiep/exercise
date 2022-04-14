package com.exercise.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcceptedResponse {
    private String deviceId;
    private String timestamp;
}
