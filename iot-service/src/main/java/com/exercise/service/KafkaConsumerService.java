package com.exercise.service;

import com.exercise.converter.DataConverter;
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

    private DataConverter dataConverter;

    @Autowired
    public KafkaConsumerService(DeviceDataService deviceDataService, DataConverter dataConverter) {
        this.deviceDataService = deviceDataService;
        this.dataConverter = dataConverter;
    }

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(DeviceItem deviceItem) {
        Optional<String> json = this.dataConverter.toJson(deviceItem);
        json.ifPresentOrElse(
                doc -> this.deviceDataService.persist(doc, deviceItem.getDeviceId()),
                        () -> log.info("Service"));
    }
}
