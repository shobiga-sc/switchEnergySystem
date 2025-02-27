package com.trustrace.switchEnergySystem.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trustrace.switchEnergySystem.entity.SmartMeter;
import com.trustrace.switchEnergySystem.repository.SmartMeterRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SmartMeterService {
    private final SmartMeterRepository smartMeterRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    public SmartMeterService(SmartMeterRepository smartMeterRepository) {
        this.smartMeterRepository = smartMeterRepository;
    }

    public List<SmartMeter> getSmartMetersByUser(String userId) {
        List<SmartMeter> smartMeters = smartMeterRepository.findTop20ByUserIdAndIsActive(userId,true);
        if (smartMeters.isEmpty()) {
            logger.warn("No active smart meters found for user ID: {}", userId);
        } else {
            logger.info("Found {} active smart meters for user ID: {}", smartMeters.size(), userId);
        }

        return smartMeters;
    }

    public SmartMeter addSmartMeter(SmartMeter smartMeter) {
        return smartMeterRepository.save(smartMeter);
    }
    public SmartMeter decommissionSmartMeter(String smartMeterId) {
        // Fetch the smart meter
        SmartMeter smartMeter = smartMeterRepository.findById(smartMeterId)
                .orElseThrow(() -> new IllegalArgumentException("SmartMeter not found for ID: " + smartMeterId));

        // Set the smart meter as inactive and update the decommissioned date
        smartMeter.setActive(false);
        smartMeter.setDecommissionedAt(new Date()); // Set current date as decommissioned date

        // Save the updated smart meter
        return smartMeterRepository.save(smartMeter);
    }

    public List<SmartMeter> getAllSmartMeters() {
        return smartMeterRepository.findTop20By();
    }
}
