package com.exercise.service;

import com.exercise.domain.DeviceItem;
import com.exercise.repository.DeviceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeviceDataServiceMongo implements DeviceDataService {

    private DeviceDataRepository repository;

    private MongoTemplate mongoTemplate;

    @Autowired
    public DeviceDataServiceMongo(DeviceDataRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void persist(DeviceItem item) {
        repository.save(item);
    }

    @Override
    public void persist(String doc, String id) {
        this.mongoTemplate.save(doc, id);
    }

    /**
     * [{
     *     $group: {
     *         _id: {
     *             deviceId: '$deviceId',
     *             longitude: '$longitude',
     *             latitude: '$latitude'
     *         },
     *         data: {
     *             $push: '$data'
     *         }
     *     }
     * }, {
     *     $project: {
     *         _id: 0,
     *         deviceId: '$_id.deviceId',
     *         longitude: '$_id.longitude',
     *         latitude: '$_id.latitude',
     *         data: '$data'
     *     }
     * }]
     *
     * Arrays.asList(new Document("$group",
     *     new Document("_id",
     *     new Document("deviceId", "$deviceId")
     *                 .append("longitude", "$longitude")
     *                 .append("latitude", "$latitude"))
     *             .append("data",
     *     new Document("$push", "$data"))),
     *     new Document("$project",
     *     new Document("_id", 0L)
     *             .append("deviceId", "$_id.deviceId")
     *             .append("longitude", "$_id.longitude")
     *             .append("latitude", "$_id.latitude")
     *             .append("data", "$data")))
     * @param deviceId
     * @param from
     * @param to
     */
    @Override
    public void find(String deviceId, LocalDateTime from, LocalDateTime to) {
    }
}
