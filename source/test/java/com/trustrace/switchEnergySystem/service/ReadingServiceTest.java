package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.entity.Reading;
import com.trustrace.switchEnergySystem.repository.ReadingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReadingServiceTest {

    @Mock
    private ReadingRepository readingRepository;

    @InjectMocks
    private ReadingService readingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddReading() {
        // Given
        String smartMeterId = "smartMeter123";
        double kWReading = 5.5;
        LocalDateTime currentTimestamp = LocalDateTime.now();

        Reading reading = new Reading(smartMeterId, kWReading, currentTimestamp);

        when(readingRepository.save(any(Reading.class))).thenReturn(reading);

        // When
        Reading result = readingService.addReading(smartMeterId, kWReading);

        // Then
        assertNotNull(result);
        assertEquals(smartMeterId, result.getSmartMeterId());
        assertEquals(kWReading, result.getKilowatt(), 0.001);
        assertEquals(currentTimestamp, result.getTimestamp());
        verify(readingRepository, times(1)).save(any(Reading.class));
    }

    @Test
    void testCalculateKWH_WithValidReadings() {
        // Given
        Reading reading1 = new Reading("smartMeter123", 60.0, LocalDateTime.now());
        Reading reading2 = new Reading("smartMeter123", 30.0, LocalDateTime.now());
        List<Reading> readings = Arrays.asList(reading1, reading2);

        // When
        double result = readingService.calculateKWH(readings);

        // Then
        assertEquals(1.5, result, 0.001); // (60 + 30) / 60 = 1.5 kWh
    }

    @Test
    void testCalculateKWH_WithNoReadings() {
        // Given
        List<Reading> readings = Collections.emptyList();

        // When
        double result = readingService.calculateKWH(readings);

        // Then
        assertEquals(0.0, result); // No readings, return 0 kWh
    }

    @Test
    void testCalculateKWH_WithNullReadings() {
        // When
        double result = readingService.calculateKWH(null);

        // Then
        assertEquals(0.0, result); // Null readings, return 0 kWh
    }

    @Test
    void testGetReadingsForSmartMeter() {
        // Given
        String smartMeterId = "smartMeter123";
        Reading reading1 = new Reading(smartMeterId, 60.0, LocalDateTime.now());
        Reading reading2 = new Reading(smartMeterId, 30.0, LocalDateTime.now());
        List<Reading> readings = Arrays.asList(reading1, reading2);

        when(readingRepository.findBySmartMeterId(smartMeterId)).thenReturn(readings);

        // When
        List<Reading> result = readingService.getReadingsForSmartMeter(smartMeterId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(smartMeterId, result.get(0).getSmartMeterId());
        assertEquals(smartMeterId, result.get(1).getSmartMeterId());
        verify(readingRepository, times(1)).findBySmartMeterId(smartMeterId);
    }
}
