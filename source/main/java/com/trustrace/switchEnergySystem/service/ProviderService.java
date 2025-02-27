package com.trustrace.switchEnergySystem.service;


import com.trustrace.switchEnergySystem.dto.CostSimulationDTO;
import com.trustrace.switchEnergySystem.dto.ProviderRateDTO;
import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    @Autowired
    private final ProviderRepository providerRepository;



    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;

    }
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }



    public List<Provider> getActiveProviders() {
        return providerRepository.findByIsActive(true);
    }



    public Provider addProvider(Provider provider) {
        logger.info("Saving provider with isActive: {}", provider.isActive());
        return providerRepository.save(provider);
    }

    public Provider disableProvider(String providerId) {
        // Fetch the provider
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Provider not found for ID: " + providerId));

        // Set the provider as inactive
        provider.setActive(false);

        // Save the updated provider
        return providerRepository.save(provider);
    }
    public List<ProviderRateDTO> getAllProvidersWithRates() {
        logger.info("Fetching all active energy providers with current rates.");
        List<Provider> providers = providerRepository.findByIsActive(true);

        // Map to DTOs
        return providers.stream()
                .map(provider -> new ProviderRateDTO(provider.getProviderName(), provider.getRatePerKWH()))
                .collect(Collectors.toList());
    }
}
