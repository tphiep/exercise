package com.exercise.service;

import com.exercise.domain.DeviceData;
import com.exercise.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<DeviceData> findBy(String deviceId, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        MatchOperation matchFromDate = Aggregation.match(new Criteria("timestamp").gte(fromDateTime));
        MatchOperation matchToDate = Aggregation.match(new Criteria("timestamp").lte(toDateTime));
        ProjectionOperation projectStage = Aggregation.project().and(AggregationExpression.from(MongoExpression
                .create("\"$mergeObjects\":[\"$data\",{\"timestamp\":{\"$dateToString\":{format:\"%Y-%m-%dT%H:%M:%S:%LZ\",\"date\":\"$timestamp\"}}}]"))).as("data");
        Aggregation aggregation
                = Aggregation.newAggregation(matchFromDate, matchToDate, projectStage);
        AggregationResults<DeviceData> data
                = mongoTemplate.aggregate(aggregation, deviceId, DeviceData.class);
        return data.getMappedResults();
    }

}
