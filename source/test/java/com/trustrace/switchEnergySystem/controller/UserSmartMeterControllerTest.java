package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.service.UserSmartMeterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserSmartMeterControllerTest {

    @Mock
    private UserSmartMeterService userSmartMeterService;

    @InjectMocks
    private UserSmartMeterController userSmartMeterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserSmartMeters() {
        // Arrange
        String userId = "user123";
        List<UserSmartMeter> smartMeters = new ArrayList<>();
        when(userSmartMeterService.getUserSmartMeters(userId)).thenReturn(smartMeters);

        // Act
        List<UserSmartMeter> result = userSmartMeterController.getUserSmartMeters(userId);

        // Assert
        verify(userSmartMeterService).getUserSmartMeters(userId);
        assertEquals(smartMeters, result);
    }

    @Test
    void testAssignSmartMeter() {
        // Arrange
        UserSmartMeter newSmartMeter = new UserSmartMeter(); // Add necessary attributes if any
        when(userSmartMeterService.assignSmartMeter(any(UserSmartMeter.class))).thenReturn(newSmartMeter);

        // Act
        UserSmartMeter result = userSmartMeterController.assignSmartMeter(newSmartMeter);

        // Assert
        verify(userSmartMeterService).assignSmartMeter(newSmartMeter);
        assertEquals(newSmartMeter, result);
    }

    @Test
    void testUpdateProvider() {
        // Arrange
        String smartMeterId = "smartMeter456";
        String providerId = "provider789";
        UserSmartMeter updatedSmartMeter = new UserSmartMeter(); // Add necessary attributes if any
        when(userSmartMeterService.updateProvider(smartMeterId, providerId)).thenReturn(updatedSmartMeter);

        // Act
        UserSmartMeter result = userSmartMeterController.updateProvider(smartMeterId, providerId);

        // Assert
        verify(userSmartMeterService).updateProvider(smartMeterId, providerId);
        assertEquals(updatedSmartMeter, result);
    }
}
