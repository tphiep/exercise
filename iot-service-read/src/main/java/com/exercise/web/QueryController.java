package com.exercise.web;

import com.exercise.domain.DeviceData;
import com.exercise.domain.Device;
import com.exercise.domain.DeviceDataResult;
import com.exercise.exception.DeviceNotFoundException;
import com.exercise.helper.DateTimeHelper;
import com.exercise.service.DeviceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<DeviceDataResult> get(
            @PathVariable String deviceId,
            @Valid @RequestParam @DateTimeFormat(pattern = DateTimeHelper.DATETIME_PATTERN) LocalDateTime fromDate,
            @Valid @RequestParam @DateTimeFormat(pattern = DateTimeHelper.DATETIME_PATTERN) LocalDateTime toDate

    ) throws DeviceNotFoundException {
        log.info("Receive request of deviceId={}", deviceId);
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(String.format("'fromDate' must be less than or equal to 'toDate'"));
        }
        Optional<Device> optionalDevice = this.deviceDataService.find(deviceId);
        Device device = optionalDevice.orElseThrow(() ->
                new DeviceNotFoundException(String.format("Device Not Found id='%s'", deviceId)));
        List<DeviceData> dataResults = this.deviceDataService.findBy(deviceId, fromDate, toDate);
        return ResponseEntity.ok(toDeviceResult(device, dataResults));
    }

    protected DeviceDataResult toDeviceResult(Device device, List<DeviceData> data) {
        DeviceDataResult deviceDataResult = new DeviceDataResult();
        deviceDataResult.setDeviceId(device.getDeviceId());
        deviceDataResult.setLatitude(device.getLatitude());
        deviceDataResult.setLongitude(device.getLongitude());
        data.stream().forEach(d -> deviceDataResult.addData(d.getData()));
        return deviceDataResult;
    }
}
