package com.exercise.service;

import com.exercise.domain.DataResult;
import com.exercise.domain.Device;
import com.exercise.domain.DeviceDataResult;
import com.exercise.exception.DeviceNotFoundException;
import com.exercise.helper.QueryHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public String find(String deviceId, String fromDateTime, String toDateTime) throws DeviceNotFoundException, JsonProcessingException {
        DeviceDataResult deviceData = this.findBy(deviceId, fromDateTime, toDateTime);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(deviceData);
    }

    public DeviceDataResult findBy(String deviceId, String fromDateTime, String toDateTime) throws DeviceNotFoundException {
        Device device = this.mongoTemplate.findById(deviceId, Device.class);
        if (device == null) throw new DeviceNotFoundException(String.format("Device Not Found id='%s'", deviceId));
        MatchOperation matchFromDate = Aggregation.match(new Criteria("data.timestamp").gte(fromDateTime));
        MatchOperation matchToDate = Aggregation.match(new Criteria("data.timestamp").lte(toDateTime));
        ProjectionOperation projectStage = Aggregation.project("data");
        Aggregation aggregation
                = Aggregation.newAggregation(matchFromDate, matchToDate, projectStage);
        AggregationResults<DataResult> output
                = mongoTemplate.aggregate(aggregation, deviceId, DataResult.class);
        List<DataResult> results = output.getMappedResults();
        DeviceDataResult deviceDataResult = new DeviceDataResult();
        deviceDataResult.setDeviceId(deviceId);
        deviceDataResult.setLatitude(device.getLatitude());
        deviceDataResult.setLongitude(device.getLongitude());
        results.stream().forEach(d -> deviceDataResult.addData(d.getData()));
        return deviceDataResult;
    }

}
