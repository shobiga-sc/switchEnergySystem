package com.trustrace.switchEnergySystem.repository;

import com.trustrace.switchEnergySystem.entity.Reading;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadingRepository extends MongoRepository<Reading, String> {
    List<Reading> findBySmartMeterId(String smartMeterId);
    List<Reading> findBySmartMeterIdAndTimestampBetween(String smartMeterId, LocalDateTime from, LocalDateTime to);
}
