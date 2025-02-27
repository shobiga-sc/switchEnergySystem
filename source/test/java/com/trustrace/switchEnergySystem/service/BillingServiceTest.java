package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.entity.Reading;
import com.trustrace.switchEnergySystem.repository.ProviderRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingServiceTest {

    @Mock
    private ReadingRepository readingRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateTotalKWh_WithValidReadings() {
        // Given
        String smartMeterId = "smartMeter123";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        Reading reading1 = new Reading(smartMeterId, 50.0, from.plusHours(1));
        Reading reading2 = new Reading(smartMeterId, 30.0, from.plusHours(2));

        when(readingRepository.findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to))
                .thenReturn(Arrays.asList(reading1, reading2));

        // When
        double totalKWh = billingService.calculateTotalKWh(smartMeterId, from, to);

        // Then
        assertEquals(80.0, totalKWh, 0.001);
        verify(readingRepository, times(1))
                .findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to);
    }

    @Test
    void testCalculateTotalKWh_WithNoReadings() {
        // Given
        String smartMeterId = "smartMeter123";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        when(readingRepository.findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to))
                .thenReturn(Collections.emptyList());

        // When
        double totalKWh = billingService.calculateTotalKWh(smartMeterId, from, to);

        // Then
        assertEquals(0.0, totalKWh, 0.001);
        verify(readingRepository, times(1))
                .findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to);
    }

    @Test
    void testCalculateCost_WithValidProvider() {
        // Given
        String smartMeterId = "smartMeter123";
        String providerId = "provider123";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        Reading reading1 = new Reading(smartMeterId, 50.0, from.plusHours(1));
        Reading reading2 = new Reading(smartMeterId, 30.0, from.plusHours(2));
        Provider provider = new Provider();
        provider.setRatePerKWH(0.15);

        when(readingRepository.findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to))
                .thenReturn(Arrays.asList(reading1, reading2));
        when(providerRepository.findById(providerId)).thenReturn(Optional.of(provider));

        // When
        double cost = billingService.calculateCost(smartMeterId, providerId, from, to);

        // Then
        assertEquals(12.0, cost, 0.001); // 80 kWh * 0.15 = 12.0
        verify(readingRepository, times(1))
                .findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to);
        verify(providerRepository, times(1)).findById(providerId);
    }

    @Test
    void testCalculateCost_WithNoReadings() {
        // Given
        String smartMeterId = "smartMeter123";
        String providerId = "provider123";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        Provider provider = new Provider();
        provider.setRatePerKWH(0.15);

        when(readingRepository.findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to))
                .thenReturn(Collections.emptyList());
        when(providerRepository.findById(providerId)).thenReturn(Optional.of(provider));

        // When
        double cost = billingService.calculateCost(smartMeterId, providerId, from, to);

        // Then
        assertEquals(0.0, cost, 0.001); // No readings, so total kWh is 0 and cost should be 0
        verify(readingRepository, times(1))
                .findBySmartMeterIdAndTimestampBetween(smartMeterId, from, to);
        verify(providerRepository, times(1)).findById(providerId);
    }

    @Test
    void testCalculateCost_WithInvalidProvider() {
        // Given
        String smartMeterId = "smartMeter123";
        String providerId = "invalidProvider";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        when(providerRepository.findById(providerId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            billingService.calculateCost(smartMeterId, providerId, from, to);
        });

        verify(providerRepository, times(1)).findById(providerId);
        verify(readingRepository, times(0)).findBySmartMeterIdAndTimestampBetween(anyString(), any(), any());
    }
}
