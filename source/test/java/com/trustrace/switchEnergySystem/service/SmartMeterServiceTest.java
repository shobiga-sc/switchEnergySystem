package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.entity.SmartMeter;
import com.trustrace.switchEnergySystem.repository.SmartMeterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmartMeterServiceTest {

    @InjectMocks
    private SmartMeterService smartMeterService;

    @Mock
    private SmartMeterRepository smartMeterRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSmartMetersByUser_ActiveMetersFound() {
        String userId = "user123";
        SmartMeter smartMeter1 = new SmartMeter();
        smartMeter1.setId("smartMeter1");
        smartMeter1.setUserId(userId);
        smartMeter1.setActive(true);

        SmartMeter smartMeter2 = new SmartMeter();
        smartMeter2.setId("smartMeter2");
        smartMeter2.setUserId(userId);
        smartMeter2.setActive(true);

        when(smartMeterRepository.findTop20ByUserIdAndIsActive(userId, true)).thenReturn(Arrays.asList(smartMeter1, smartMeter2));

        List<SmartMeter> result = smartMeterService.getSmartMetersByUser(userId);

        assertEquals(2, result.size());
        verify(smartMeterRepository).findTop20ByUserIdAndIsActive(userId, true);
    }

    @Test
    void testGetSmartMetersByUser_NoActiveMeters() {
        String userId = "user123";

        when(smartMeterRepository.findTop20ByUserIdAndIsActive(userId, true)).thenReturn(Arrays.asList());

        List<SmartMeter> result = smartMeterService.getSmartMetersByUser(userId);

        assertTrue(result.isEmpty());
        verify(smartMeterRepository).findTop20ByUserIdAndIsActive(userId, true);
    }

    @Test
    void testAddSmartMeter() {
        SmartMeter smartMeter = new SmartMeter();
        smartMeter.setId("smartMeter1");
        smartMeter.setUserId("user123");
        smartMeter.setActive(true);

        when(smartMeterRepository.save(smartMeter)).thenReturn(smartMeter);

        SmartMeter result = smartMeterService.addSmartMeter(smartMeter);

        assertNotNull(result);
        assertEquals("smartMeter1", result.getId());
        verify(smartMeterRepository).save(smartMeter);
    }

    @Test
    void testDecommissionSmartMeter() {
        String smartMeterId = "smartMeter1";
        SmartMeter smartMeter = new SmartMeter();
        smartMeter.setId(smartMeterId);
        smartMeter.setActive(true);

        when(smartMeterRepository.findById(smartMeterId)).thenReturn(Optional.of(smartMeter));
        when(smartMeterRepository.save(smartMeter)).thenReturn(smartMeter);

        SmartMeter result = smartMeterService.decommissionSmartMeter(smartMeterId);

        assertFalse(result.isActive());
        assertNotNull(result.getDecommissionedAt());
        verify(smartMeterRepository).findById(smartMeterId);
        verify(smartMeterRepository).save(smartMeter);
    }

    @Test
    void testDecommissionSmartMeter_NotFound() {
        String smartMeterId = "invalidSmartMeterId";

        when(smartMeterRepository.findById(smartMeterId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            smartMeterService.decommissionSmartMeter(smartMeterId);
        });

        assertEquals("SmartMeter not found for ID: " + smartMeterId, exception.getMessage());
        verify(smartMeterRepository).findById(smartMeterId);
    }

    @Test
    void testGetAllSmartMeters() {
        SmartMeter smartMeter1 = new SmartMeter();
        smartMeter1.setId("smartMeter1");

        SmartMeter smartMeter2 = new SmartMeter();
        smartMeter2.setId("smartMeter2");

        when(smartMeterRepository.findTop20By()).thenReturn(Arrays.asList(smartMeter1, smartMeter2));

        List<SmartMeter> result = smartMeterService.getAllSmartMeters();

        assertEquals(2, result.size());
        verify(smartMeterRepository).findTop20By();
    }
}
