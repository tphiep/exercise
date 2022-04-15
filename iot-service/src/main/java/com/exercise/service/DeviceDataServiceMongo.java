package com.exercise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


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
    public void persist(String doc, String id) {
        this.mongoTemplate.save(doc, id);
    }

}
