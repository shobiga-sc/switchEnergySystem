package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.entity.Reading;
import com.trustrace.switchEnergySystem.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ReadingService {
    private final ReadingRepository readingRepository;

    public ReadingService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public Reading addReading(String smartMeterId, double kWReading) {
        LocalDateTime currentTimestamp = LocalDateTime.now();
        Reading reading = new Reading(smartMeterId, kWReading, currentTimestamp);
        return readingRepository.save(reading);
    }


    public double calculateKWH(List<Reading> readings) {
        if (readings == null || readings.isEmpty()) {
            return 0.0; // No readings available, return 0 kWh
        }

        return readings.stream()
                .mapToDouble(Reading::getKilowatt) // Make sure the field name matches the entity
                .sum() / 60.0; // Assuming readings are in kW per minute
    }

    public List<Reading> getReadingsForSmartMeter(String smartMeterId) {
        return readingRepository.findBySmartMeterId(smartMeterId);
    }
}
