package com.exercise.repository;

import com.exercise.domain.DeviceItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceDataRepository extends MongoRepository<DeviceItem, String> {

}
