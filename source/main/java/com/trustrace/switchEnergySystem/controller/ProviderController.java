package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.entity.SmartMeter;
import com.trustrace.switchEnergySystem.service.AuthService;
import com.trustrace.switchEnergySystem.service.ProviderService;
import com.trustrace.switchEnergySystem.service.SmartMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.List;

@RestController
@RequestMapping("/api/providers")
@PreAuthorize("hasRole('ADMIN')")
public class ProviderController {
    @Autowired
    private AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<String> testAccess(Authentication authentication) {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        return ResponseEntity.ok("Access granted for user: " + authentication.getName() + " with roles: " + authorities);
    }

    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
    private final ProviderService providerService;
    private final SmartMeterService smartMeterService;
    public ProviderController(ProviderService providerService, SmartMeterService smartMeterService) {
        this.providerService = providerService;
        this.smartMeterService = smartMeterService;
    }

    // Endpoint to get all providers
    @GetMapping("/all")
    public List<Provider> getAllProviders() {
        logger.info("Request to view energy providers that are available");
        return providerService.getAllProviders();
    }

    // Other methods for managing providers
    @GetMapping("/active")
    public List<Provider> getActiveProviders() {
        logger.info("Request to view all active energy providers");
        return providerService.getActiveProviders();
    }

    @PostMapping("/add")
    public Provider addProvider(@RequestBody Provider provider) {
        logger.info("Request to add an energy providers");
        return providerService.addProvider(provider);
    }

    @PatchMapping("/{providerId}/disable")
    public Provider disableProvider(@PathVariable String providerId) {
        logger.info("Disabling provider with ID: {}", providerId);
        return providerService.disableProvider(providerId);
    }
    @PatchMapping("/smartMeters/{smartMeterId}/decommission")
    public SmartMeter decommissionSmartMeter(@PathVariable String smartMeterId) {
        logger.info("Decommissioning smart meter with ID: {}", smartMeterId);

        return smartMeterService.decommissionSmartMeter(smartMeterId);
    }


}
