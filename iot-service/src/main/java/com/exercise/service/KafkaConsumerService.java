package com.exercise.service;

import com.exercise.helper.CustomMapper;
import com.exercise.domain.DeviceItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(DeviceItem deviceItem) {
        Optional<String> json = this.customMapper.toJson(deviceItem);
        json.ifPresentOrElse(
                doc -> this.deviceDataService.persist(doc, deviceItem.getDeviceId()),
                        () -> log.info("Invalid message from device {}", deviceItem.getDeviceId()));
    }
}
