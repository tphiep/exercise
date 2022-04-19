package com.exercise.service;

import com.exercise.domain.Device;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
//        System.out.println(this.findBy(deviceId, fromDateTime, toDateTime));
//        String query = this.queryHelper.buildGetDeviceDataQuery(deviceId, fromDateTime, toDateTime);
//        Document document = mongoTemplate.executeCommand(query);
//        return this.queryHelper.getJsonResult(document);
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

    public static class DataResult {
        private String deviceId;
        private Double longitude;
        private Double latitude;
        private Map<String, Object> data;

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
    }

    public static class DeviceDataResult {
        private String deviceId;
        private Double longitude;
        private Double latitude;
        List<Map<String, Object>> data = new ArrayList<>();

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public void addData(Map<String, Object> deviceData) {
            this.data.add(deviceData);
        }

        public List<Map<String, Object>> getData() {
            return data;
        }
    }
}
