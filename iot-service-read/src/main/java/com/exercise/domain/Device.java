package com.exercise.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "devices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private String deviceId;
    private double longitude;
    private double latitude;
}
