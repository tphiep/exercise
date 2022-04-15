package com.exercise.service;

import com.exercise.helper.QueryHelper;
import com.exercise.repository.DeviceDataRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class DeviceDataServiceMongo implements DeviceDataService {

    private DeviceDataRepository repository;

    private MongoTemplate mongoTemplate;

    private QueryHelper queryHelper;

    @Autowired
    public DeviceDataServiceMongo(DeviceDataRepository repository,
                                  MongoTemplate mongoTemplate,
                                  QueryHelper queryHelper) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.queryHelper = queryHelper;
    }

    @Override
    public void persist(String doc, String id) {
        this.mongoTemplate.save(doc, id);
    }

    @Override
    public String find(String deviceId, String fromDateTime, String toDateTime) {
        String query = this.queryHelper.buildGetDeviceDataQuery(deviceId, fromDateTime, toDateTime);
        Document document = mongoTemplate.executeCommand(query);
        return this.queryHelper.getJsonResult(document);
    }
}
