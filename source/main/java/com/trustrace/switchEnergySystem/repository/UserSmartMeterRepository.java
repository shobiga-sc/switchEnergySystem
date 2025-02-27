package com.trustrace.switchEnergySystem.repository;


import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserSmartMeterRepository extends MongoRepository<UserSmartMeter, String> {
    List<UserSmartMeter> findByUserId(String userId);
    UserSmartMeter findBySmartMeterId(String smartMeterId);

    Optional<UserSmartMeter> findByUserIdAndSmartMeterId(String userId, String smartMeterId);

}

