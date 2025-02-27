package com.trustrace.switchEnergySystem.repository;


import com.trustrace.switchEnergySystem.entity.ProviderSwitchLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProviderSwitchLogRepository extends MongoRepository<ProviderSwitchLog, String> {
    List<ProviderSwitchLog> findByUserId(String userId);
}

