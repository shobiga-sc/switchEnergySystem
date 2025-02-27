package com.trustrace.switchEnergySystem.controller;


import com.trustrace.switchEnergySystem.entity.SmartMeter;
import com.trustrace.switchEnergySystem.service.SmartMeterService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.List;

@RestController
@RequestMapping("/api/smartmeters")
//@PreAuthorize("hasRole('SMARTMETER_API')")
public class SmartMeterController {
    private final SmartMeterService smartMeterService;
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    public SmartMeterController(SmartMeterService smartMeterService) {
        this.smartMeterService = smartMeterService;
    }

    @GetMapping("/user/{userId}")
    public List<SmartMeter> getSmartMeters(@PathVariable String userId) {
        logger.info("Request to show the user's smartmeters with user ID: {}", userId);
        return smartMeterService.getSmartMetersByUser(userId);
    }
    @GetMapping("/all")
    public List<SmartMeter> getSmartMeters() {
        logger.info("Request to show all meters");
        return smartMeterService.getAllSmartMeters();
    }


    @PostMapping("/add")
    public SmartMeter addSmartMeter(@RequestBody SmartMeter smartMeter) {
        logger.info("Request to add a new smartmeter");
        return smartMeterService.addSmartMeter(smartMeter);
    }
}
