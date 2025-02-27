package com.trustrace.switchEnergySystem.service;


import com.trustrace.switchEnergySystem.dto.CostSimulationDTO;
import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.entity.User;
import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.repository.ProviderRepository;
import com.trustrace.switchEnergySystem.repository.UserRepository;
import com.trustrace.switchEnergySystem.repository.UserSmartMeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final BillingService billingService;
    private final UserSmartMeterRepository userSmartMeterRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       ProviderRepository providerRepository,
                       BillingService billingService,
                       UserSmartMeterRepository userSmartMeterRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
        this.billingService = billingService;
        this.userSmartMeterRepository = userSmartMeterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create or register a new user






    public User addUser(User user) {
        // Hash password before saving
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    // Get a user by ID
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user by ID
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }
    public List<CostSimulationDTO> simulateCostsAll(String smartMeterId, LocalDateTime from, LocalDateTime to) {
        List<Provider> providers = providerRepository.findAll();
        List<CostSimulationDTO> simulations = new ArrayList<>();

        for (Provider provider : providers) {


            double cost = billingService.calculateCost(smartMeterId, provider.getId(), from, to);
            simulations.add(new CostSimulationDTO(provider.getProviderName(), cost));
        }

        return simulations;
    }
    public CostSimulationDTO simulateCosts(String smartMeterId, LocalDateTime from, LocalDateTime to) {
        // Find user smart meter
        UserSmartMeter userSmartMeter = userSmartMeterRepository.findBySmartMeterId(smartMeterId);
        if (userSmartMeter == null) {
            throw new IllegalArgumentException("Smart meter not found for ID: " + smartMeterId);
        }

        // Get provider ID and find provider
        String providerId = userSmartMeter.getProviderId();
        Optional<Provider> optionalProvider = providerRepository.findById(providerId);
        if (!optionalProvider.isPresent() || !optionalProvider.get().isActive()) {
            throw new IllegalArgumentException("Provider not found for ID: " + providerId);
        }

        Provider provider = optionalProvider.get();

        // Calculate cost
        double cost = billingService.calculateCost(smartMeterId, provider.getId(), from, to);

        // Create and return the CostSimulationDTO
        return new CostSimulationDTO(provider.getProviderName(), cost);
    }



}

