package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.entity.Reading;
import com.trustrace.switchEnergySystem.service.ReadingService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class ReadingController {
    private final ReadingService readingService;
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @PostMapping("/{smartMeterId}")
    public Reading addReading(@PathVariable String smartMeterId, @RequestParam double kWReading) {
        logger.info("Request to add reading to a smart meter with ID: {}", smartMeterId);
        return readingService.addReading(smartMeterId, kWReading);
    }


    @GetMapping("/{smartMeterId}/kwh")
    public double calculateKWH(@PathVariable String smartMeterId) {
        List<Reading> readings = readingService.getReadingsForSmartMeter(smartMeterId);
        logger.info("Request to convert reading to a KWH for smart meter with ID: {}", smartMeterId);
        return readingService.calculateKWH(readings);
    }
}
