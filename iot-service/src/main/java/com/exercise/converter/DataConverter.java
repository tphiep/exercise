package com.exercise.converter;

import com.exercise.domain.DeviceItem;
import com.exercise.request.CreateDeviceDataRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DataConverter {

    public DeviceItem toDeviceItem(String timestamp, CreateDeviceDataRequest request) {
        DeviceItem item = new DeviceItem();
        item.setDeviceId(request.getDeviceId());
        item.setData(request.getData());
        item.setLongitude(request.getLongitude());
        item.setLatitude(request.getLatitude());
        JsonNode node = item.getData();
        ((ObjectNode) node).put("timestamp", timestamp);
        return item;
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
