package com.exercise.helper;

import com.exercise.domain.DeviceData;
import com.exercise.domain.DeviceItem;
import com.exercise.request.CreateDeviceDataRequest;
import com.exercise.response.AcceptedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class CustomMapper extends ObjectMapper {

    public DeviceItem toDeviceItem(CreateDeviceDataRequest request) {
        String timestamp = DateTimeHelper.formatDateTime(LocalDateTime.now());
        DeviceItem item = new DeviceItem.Builder()
                .deviceId(request.getDeviceId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .data(request.getData())
                .timestamp(timestamp)
                .build();
        return item;
    }

    public AcceptedResponse toAcceptedResponse(DeviceItem item) {
        AcceptedResponse acceptedResponse = AcceptedResponse.builder()
                .deviceId(item.getDeviceId())
                .timestamp(item.getTimestamp())
                .build();
        return acceptedResponse;
    }

    public Optional<String> toJson(DeviceItem item) {
        String json = null;
        try {
            this.writer().withDefaultPrettyPrinter();
            json = this.writeValueAsString(fromDeviceItem(item));
        } catch (JsonProcessingException e) {
            log.error("Unable to convert data from deviceId {}", item.getDeviceId(), e);
        }
        return Optional.of(json);
    }

    public DeviceData fromDeviceItem(DeviceItem item) {
        DeviceData deviceData = new DeviceData(item.getData());
        ObjectNode dataNode = (ObjectNode) deviceData.getData();
        dataNode.put("timestamp", item.getTimestamp());
        return deviceData;
    }
}
