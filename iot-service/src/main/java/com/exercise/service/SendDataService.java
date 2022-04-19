package com.exercise.service;

import com.exercise.domain.DeviceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendDataService {

    private String topic;

    private KafkaTemplate<String, DeviceItem> kafkaTemplate;

    public SendDataService(@Value("${kafka.topic}") String topic,
                           @Autowired KafkaTemplate<String, DeviceItem> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Send CREATE EVENT to kafka
     * @param data
     */
    public void send(DeviceItem data) {
        kafkaTemplate.send(topic, data.getDeviceId(), data);
    }

}
