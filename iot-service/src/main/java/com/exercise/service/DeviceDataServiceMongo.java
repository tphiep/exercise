package com.exercise.service;

import com.exercise.exception.MongoDBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceDataServiceMongo implements DeviceDataService {

    private MongoTemplate mongoTemplate;

    @Autowired
    public DeviceDataServiceMongo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Add new data document to device collection
     * @param doc
     * @param id
     */
    @Override
    public void persist(String json, String id) {
        try {
            this.mongoTemplate.save(json, id);
        } catch (Throwable e) {
            log.error("Failed to persist device data id='{}' with message '{}' ", id, e);
            throw new MongoDBException(e.getMessage());
        }
    }

}
