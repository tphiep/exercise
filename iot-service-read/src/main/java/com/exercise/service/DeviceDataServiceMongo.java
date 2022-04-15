package com.exercise.service;

import com.exercise.helper.QueryHelper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class DeviceDataServiceMongo implements DeviceDataService {

    private MongoTemplate mongoTemplate;

    private QueryHelper queryHelper;

    @Autowired
    public DeviceDataServiceMongo(MongoTemplate mongoTemplate,
                                  QueryHelper queryHelper) {
        this.mongoTemplate = mongoTemplate;
        this.queryHelper = queryHelper;
    }

    /**
     * Execute aggregate query to find time series of given device id
     * @param deviceId
     * @param fromDateTime
     * @param toDateTime
     * @return
     */
    @Override
    public String find(String deviceId, String fromDateTime, String toDateTime) {
        String query = this.queryHelper.buildGetDeviceDataQuery(deviceId, fromDateTime, toDateTime);
        Document document = mongoTemplate.executeCommand(query);
        return this.queryHelper.getJsonResult(document);
    }
}
