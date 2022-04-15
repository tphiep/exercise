package com.exercise.web;

import com.exercise.helper.DataConverter;
import com.exercise.domain.DeviceItem;
import com.exercise.request.CreateDeviceDataRequest;
import com.exercise.response.AcceptedResponse;
import com.exercise.service.SendDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path="/api")
public class CreateController {

    private SendDataService sendDataService;

    private DataConverter dataConverter;

    public CreateController(SendDataService sendDataService, DataConverter dataConverter) {
        this.sendDataService = sendDataService;
        this.dataConverter = dataConverter;
    }

    @PostMapping(value = "/devices",
            produces= MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcceptedResponse> create(@Valid @RequestBody CreateDeviceDataRequest request) {
        log.info("Received data from device id={}", request.getDeviceId());
        DeviceItem item = this.dataConverter.toDeviceItem(request);
        this.sendDataService.send(item);
        return ResponseEntity.accepted().body(this.dataConverter.toAcceptedResponse(item));
    }

}
