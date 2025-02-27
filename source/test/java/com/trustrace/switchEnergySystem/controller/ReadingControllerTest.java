package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.entity.Reading;
import com.trustrace.switchEnergySystem.service.ReadingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReadingControllerTest {

    @Mock
    private ReadingService readingService;

    @InjectMocks
    private ReadingController readingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddReading() {
        // Arrange
        String smartMeterId = "smartMeter123";
        double kWReading = 5.0;
        LocalDateTime timestamp = LocalDateTime.now(); // Create a LocalDateTime object
        Reading expectedReading = new Reading(smartMeterId, kWReading, timestamp); // Use constructor
        when(readingService.addReading(smartMeterId, kWReading)).thenReturn(expectedReading);

        // Act
        Reading actualReading = readingController.addReading(smartMeterId, kWReading);

        // Assert
        assertEquals(expectedReading, actualReading);
        verify(readingService).addReading(smartMeterId, kWReading);
    }

    @Test
    void testCalculateKWH() {
        // Arrange
        String smartMeterId = "smartMeter123";
        List<Reading> mockReadings = new ArrayList<>();
        LocalDateTime timestamp = LocalDateTime.now(); // Create a LocalDateTime object
        mockReadings.add(new Reading(smartMeterId, 5.0, timestamp)); // Use constructor to create Reading
        when(readingService.getReadingsForSmartMeter(smartMeterId)).thenReturn(mockReadings);
        when(readingService.calculateKWH(mockReadings)).thenReturn(10.0); // Expected kWh result

        // Act
        double kWh = readingController.calculateKWH(smartMeterId);

        // Assert
        assertEquals(10.0, kWh);
        verify(readingService).getReadingsForSmartMeter(smartMeterId);
        verify(readingService).calculateKWH(mockReadings);
    }
}
