package com.exercise.helper;

import com.exercise.domain.DeviceItem;
import com.exercise.request.CreateDeviceDataRequest;
import com.exercise.response.AcceptedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class DataConverter {

    public DeviceItem toDeviceItem(CreateDeviceDataRequest request) {
        String timestamp = DateTimeHelper.formatDateTime(LocalDateTime.now());
        DeviceItem item = new DeviceItem();
        item.setDeviceId(request.getDeviceId());
        item.setData(request.getData());
        item.setLongitude(request.getLongitude());
        item.setLatitude(request.getLatitude());
        item.setTimestamp(timestamp);
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
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert from deviceId {}", item.getDeviceId());
            e.printStackTrace();
        }
        return Optional.of(json);
    }
}
