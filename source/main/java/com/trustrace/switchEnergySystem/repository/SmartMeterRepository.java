package com.trustrace.switchEnergySystem.repository;


import com.trustrace.switchEnergySystem.entity.SmartMeter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SmartMeterRepository extends MongoRepository<SmartMeter, String> {
    List<SmartMeter> findByUserId(String userId);
    List<SmartMeter> findTop20ByUserIdAndIsActive(String userId, boolean isActive);
    @Query("{}")
    List<SmartMeter> findTop20By();
}
