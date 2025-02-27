package com.trustrace.switchEnergySystem.service;


import com.trustrace.switchEnergySystem.entity.ProviderSwitchLog;
import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.repository.ProviderSwitchLogRepository;
import com.trustrace.switchEnergySystem.repository.UserSmartMeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class ProviderSwitchService {

    @Autowired
    private UserSmartMeterRepository userSmartMeterRepository;

    @Autowired
    private ProviderSwitchLogRepository providerSwitchLogRepository;

    public void switchProvider(String userId, String smartMeterId, String newProviderId) {
        UserSmartMeter userSmartMeter = userSmartMeterRepository
                .findByUserIdAndSmartMeterId(userId, smartMeterId)
                .orElseThrow(() -> new RuntimeException("Smart meter not found"));

        String oldProviderId = userSmartMeter.getProviderId();
        userSmartMeter.setProviderId(newProviderId);
        userSmartMeterRepository.save(userSmartMeter);

        ProviderSwitchLog log = new ProviderSwitchLog();
        log.setUserId(userId);
        log.setSmartMeterId(smartMeterId);
        log.setOldProviderId(oldProviderId);
        log.setNewProviderId(newProviderId);
        log.setSwitchDate(new Date());

        providerSwitchLogRepository.save(log);
    }
}
