package com.exercise.service;

import com.exercise.domain.Device;
import com.exercise.domain.DeviceData;
import com.exercise.exception.MongoDBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param device
     * @param deviceData
     * @param id
     */
    @Transactional
    @Override
    public void persist(Device device, DeviceData deviceData, String id) {
        try {
            Device fromDb = this.mongoTemplate.findById(device.getDeviceId(), Device.class);
            if (fromDb == null || !fromDb.equals(device)) {
                log.info("Save or update device info {}", device);
                this.mongoTemplate.save(device);
            }
            this.mongoTemplate.save(deviceData, id);
            if (fromDb == null) {
                log.info("Create index for data of collection = '{}'", device.getDeviceId());
                this.mongoTemplate.indexOps(device.getDeviceId()).ensureIndex(new Index().on("timestamp", Sort.Direction.ASC));
            }
        } catch (Throwable e) {
            log.error("Failed to persist device data id='{}' with message '{}' ", id, e);
            throw new MongoDBException(e.getMessage());
        }
    }
}
