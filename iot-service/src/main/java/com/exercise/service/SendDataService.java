package com.exercise.service;

import com.exercise.domain.DeviceItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
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
//    @Async("sendExecutor")
    public void send(DeviceItem data) {
        ListenableFuture<SendResult<String, DeviceItem>> result = kafkaTemplate.send(topic, data.getDeviceId(), data);
        result.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(final SendResult<String, DeviceItem> message) {
                log.info("Sent message= " + data + " with offset= " + message.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(final Throwable throwable) {
                log.error("Unable to send message= " + data, throwable);
            }
        });
    }

}
