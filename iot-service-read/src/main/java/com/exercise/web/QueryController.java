package com.exercise.web;

import com.exercise.exception.DeviceNotFoundException;
import com.exercise.helper.DateTimeHelper;
import com.exercise.service.DeviceDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(path="/api")
public class QueryController {

    private DeviceDataService deviceDataService;

    public QueryController(@Autowired DeviceDataService deviceDataService) {
        this.deviceDataService = deviceDataService;
    }

    @GetMapping(value = "/devices/{deviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> get(
            @PathVariable String deviceId,
            @Valid @RequestParam @DateTimeFormat(pattern = DateTimeHelper.DATETIME_PATTERN) LocalDateTime fromDate,
            @Valid @RequestParam @DateTimeFormat(pattern = DateTimeHelper.DATETIME_PATTERN) LocalDateTime toDate

    ) throws DeviceNotFoundException, JsonProcessingException {
        log.info("Receive request of deviceId={}", deviceId);
        String result = this.deviceDataService.find(deviceId,
                DateTimeHelper.formatDateTime(fromDate),
                DateTimeHelper.formatDateTime(toDate));
        return ResponseEntity.ok(result);
    }
}
