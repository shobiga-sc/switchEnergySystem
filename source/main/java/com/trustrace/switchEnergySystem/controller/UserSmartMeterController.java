package com.trustrace.switchEnergySystem.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.service.UserSmartMeterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userSmartMeters")
public class UserSmartMeterController {
    private final UserSmartMeterService userSmartMeterService;
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    public UserSmartMeterController(UserSmartMeterService userSmartMeterService) {
        this.userSmartMeterService = userSmartMeterService;
    }

    @GetMapping("/{userId}")
    public List<UserSmartMeter> getUserSmartMeters(@PathVariable String userId) {
        logger.info("Request to show the smartmeters that belongs to user with the ID {} ",userId);
        return userSmartMeterService.getUserSmartMeters(userId);
    }

    @PostMapping("/assign")
    public UserSmartMeter assignSmartMeter(@RequestBody UserSmartMeter userSmartMeter) {
        logger.info("Request to assign a smartmeter under user ");
        return userSmartMeterService.assignSmartMeter(userSmartMeter);
    }

    @PutMapping("/updateProvider/{smartMeterId}/{providerId}")
    public UserSmartMeter updateProvider(@PathVariable String smartMeterId, @PathVariable String providerId) {
        logger.info("Request to switch provider for smartmeter with the ID {} ",smartMeterId);
        return userSmartMeterService.updateProvider(smartMeterId, providerId);
    }
}
