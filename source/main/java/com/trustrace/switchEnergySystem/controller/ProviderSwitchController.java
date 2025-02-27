package com.trustrace.switchEnergySystem.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.service.ProviderSwitchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
public class ProviderSwitchController {

    private final ProviderSwitchService providerSwitchService;
    private static final Logger logger = LoggerFactory.getLogger(ProviderSwitchController.class);

    public ProviderSwitchController(ProviderSwitchService providerSwitchService) {
        this.providerSwitchService = providerSwitchService;
    }
    @PostMapping("/switch-provider")
    public ResponseEntity<?> switchProvider(@RequestParam String userId,
                                            @RequestParam String smartMeterId,
                                            @RequestParam String newProviderId) {
        logger.info("Request to switch provider for the  smart meter with ID : {} to the new provider with ID : {} ",smartMeterId,newProviderId);
        providerSwitchService.switchProvider(userId, smartMeterId, newProviderId);
        return ResponseEntity.ok("Provider switched successfully");
    }



}
