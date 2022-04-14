package com.exercise.web;

import com.exercise.converter.DataConverter;
import com.exercise.domain.DeviceItem;
import com.exercise.request.CreateDeviceDataRequest;
import com.exercise.response.AcceptedResponse;
import com.exercise.service.SendDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path="/api")
public class DeviceController {

    private SendDataService sendDataService;

    private DataConverter dataConverter;

    public DeviceController(SendDataService sendDataService, DataConverter dataConverter) {
        this.sendDataService = sendDataService;
        this.dataConverter = dataConverter;
    }

    @PostMapping(value = "/devices",
            produces= MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcceptedResponse> create(@RequestBody CreateDeviceDataRequest request) {
        log.info("Received data from device id={}", request.getDeviceId());
        String timestamp = Instant.now().toString();
        DeviceItem item = this.dataConverter.toDeviceItem(timestamp, request);
        this.sendDataService.send(item);
        AcceptedResponse response = AcceptedResponse.builder()
                .deviceId(item.getDeviceId())
                .timestamp(timestamp)
                .build();
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping(value = "/ping",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Integer>> ping() {
        Map<String, Integer> res = new HashMap<>();
        res.put("status", 200);
        return ResponseEntity.ok(res);
    }
}
