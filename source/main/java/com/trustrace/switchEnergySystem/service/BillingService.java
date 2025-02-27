package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.entity.Reading;
import com.trustrace.switchEnergySystem.repository.ProviderRepository;
import com.trustrace.switchEnergySystem.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    @Autowired
    private ReadingRepository readingRepository;

    @Autowired
    private ProviderRepository providerRepository;

    // Calculate total kWh consumption for the given period (daily/weekly/monthly)
    public double calculateTotalKWh(String smartMeterId, LocalDateTime from, LocalDateTime to) {
        List<Reading> readings = readingRepository.findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to);
        return readings.stream().mapToDouble(Reading::getKilowatt).sum();
    }

    // Calculate total cost using the provider's rate
    public double calculateCost(String smartMeterId, String providerId, LocalDateTime from, LocalDateTime to) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Provider not found for ID: " + providerId));
        double totalKWh = calculateTotalKWh(smartMeterId, from, to);
        return totalKWh * provider.getRatePerKWH();
    }

}
