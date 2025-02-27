package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.service.ProviderSwitchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProviderSwitchControllerTest {

    @Mock
    private ProviderSwitchService providerSwitchService;

    @InjectMocks
    private ProviderSwitchController providerSwitchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSwitchProvider() {
        // Arrange
        String userId = "user123";
        String smartMeterId = "smartMeter456";
        String newProviderId = "provider789";

        // Act
        ResponseEntity<?> response = providerSwitchController.switchProvider(userId, smartMeterId, newProviderId);

        // Assert
        verify(providerSwitchService).switchProvider(userId, smartMeterId, newProviderId);
        // Verify that the response status is OK and the message is as expected
        assertEquals(ResponseEntity.ok("Provider switched successfully"), response);
    }
}
