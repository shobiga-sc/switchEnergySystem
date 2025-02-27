package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.entity.ProviderSwitchLog;
import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.repository.ProviderSwitchLogRepository;
import com.trustrace.switchEnergySystem.repository.UserSmartMeterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProviderSwitchServiceTest {

    @Mock
    private UserSmartMeterRepository userSmartMeterRepository;

    @Mock
    private ProviderSwitchLogRepository providerSwitchLogRepository;

    @InjectMocks
    private ProviderSwitchService providerSwitchService;

    private UserSmartMeter userSmartMeter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userSmartMeter = new UserSmartMeter();
        userSmartMeter.setUserId("user123");
        userSmartMeter.setSmartMeterId("smartMeter456");
        userSmartMeter.setProviderId("provider789");
    }

    @Test
    void testSwitchProvider() {
        // Arrange
        String newProviderId = "provider101112";
        when(userSmartMeterRepository.findByUserIdAndSmartMeterId("user123", "smartMeter456"))
                .thenReturn(Optional.of(userSmartMeter));

        // Act
        providerSwitchService.switchProvider("user123", "smartMeter456", newProviderId);

        // Assert
        verify(userSmartMeterRepository).findByUserIdAndSmartMeterId("user123", "smartMeter456");
        verify(userSmartMeterRepository).save(userSmartMeter);
        verify(providerSwitchLogRepository).save(any(ProviderSwitchLog.class));

        // Verify the updated provider ID
        assertEquals(newProviderId, userSmartMeter.getProviderId());
    }

    @Test
    void testSwitchProvider_SmartMeterNotFound() {
        // Arrange
        when(userSmartMeterRepository.findByUserIdAndSmartMeterId("user123", "smartMeter456"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                providerSwitchService.switchProvider("user123", "smartMeter456", "newProviderId"));

        assertEquals("Smart meter not found", exception.getMessage());
    }
}
