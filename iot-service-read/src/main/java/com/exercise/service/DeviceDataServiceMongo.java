package com.exercise.service;

import com.exercise.domain.DeviceData;
import com.exercise.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceDataServiceMongo implements DeviceDataService {

    private MongoTemplate mongoTemplate;

    @Autowired
    public DeviceDataServiceMongo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Optional<Device> find(String deviceId) {
        return Optional.ofNullable(this.mongoTemplate.findById(deviceId, Device.class));
    }

    /**
     * Execute aggregate query to find time series of given device id
     * @param deviceId
     * @param fromDateTime
     * @param toDateTime
     * @return
     */
    public List<DeviceData> findBy(String deviceId, String fromDateTime, String toDateTime) {
        MatchOperation matchFromDate = Aggregation.match(new Criteria("data.timestamp").gte(fromDateTime));
        MatchOperation matchToDate = Aggregation.match(new Criteria("data.timestamp").lte(toDateTime));
        ProjectionOperation projectStage = Aggregation.project("data");
        Aggregation aggregation
                = Aggregation.newAggregation(matchFromDate, matchToDate, projectStage);
        AggregationResults<DeviceData> data
                = mongoTemplate.aggregate(aggregation, deviceId, DeviceData.class);
        return data.getMappedResults();
    }

}
