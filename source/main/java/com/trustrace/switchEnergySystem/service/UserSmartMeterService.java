package com.trustrace.switchEnergySystem.service;


import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.repository.UserSmartMeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSmartMeterService {

    @Autowired
    private final UserSmartMeterRepository userSmartMeterRepository;

    public UserSmartMeterService(UserSmartMeterRepository userSmartMeterRepository) {
        this.userSmartMeterRepository = userSmartMeterRepository;
    }

    // Assign a smart meter to a user
    public UserSmartMeter assignSmartMeter(UserSmartMeter userSmartMeter) {
        return userSmartMeterRepository.save(userSmartMeter);
    }

    // Get all smart meters assigned to a user
    public List<UserSmartMeter> getUserSmartMeters(String userId) {
        return userSmartMeterRepository.findByUserId(userId);
    }

    // Update provider for a smart meter
    public UserSmartMeter updateProvider(String smartMeterId, String providerId) {
        UserSmartMeter userSmartMeter = userSmartMeterRepository.findBySmartMeterId(smartMeterId);
        userSmartMeter.setProviderId(providerId);
        return userSmartMeterRepository.save(userSmartMeter);
    }
}
