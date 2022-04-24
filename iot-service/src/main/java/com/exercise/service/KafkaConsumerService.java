package com.exercise.service;

import com.exercise.domain.Device;
import com.exercise.domain.DeviceData;
import com.exercise.helper.CustomMapper;
import com.exercise.domain.DeviceItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
public class KafkaConsumerService {

    private DeviceDataService deviceDataService;

    private CustomMapper customMapper;

    @Autowired
    public KafkaConsumerService(DeviceDataService deviceDataService, CustomMapper customMapper) {
        this.deviceDataService = deviceDataService;
        this.customMapper = customMapper;
    }

    /**
     * Process CREATE Device Data Event, convert data event to document then persist to mongodb
     * @param deviceItem
     */
    @RetryableTopic(attempts = "2",
            backoff = @Backoff(delay = 100, multiplier = 2.0))
    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(DeviceItem deviceItem) {
        log.info("Consume {}", deviceItem);
//        Optional<String> json = this.customMapper.toJson(deviceItem);
        DeviceData deviceData = new DeviceData(deviceItem.getData());
        deviceData.setTimestamp(LocalDateTime.now());
        Device device = Device.builder()
                .deviceId(deviceItem.getDeviceId())
                .longitude(deviceItem.getLongitude())
                .latitude(deviceItem.getLatitude())
                .build();
        this.deviceDataService.persist(device, deviceData, deviceItem.getDeviceId());
//        json.ifPresentOrElse(
//
//        doc -> this.deviceDataService.persist(device, deviceData, deviceItem.getDeviceId()),
//                () -> log.info("Invalid message from device {}", deviceItem));

    }

    @DltHandler
    public void handleDeadMessage(DeviceItem deviceItem, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Data from device {} sent to dead letter queue: {}", deviceItem, topic);
    }
}
