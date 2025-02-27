package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.entity.SmartMeter;
import com.trustrace.switchEnergySystem.service.SmartMeterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SmartMeterControllerTest {

    @InjectMocks
    private SmartMeterController smartMeterController;

    @Mock
    private SmartMeterService smartMeterService;

    private SmartMeter smartMeter1;
    private SmartMeter smartMeter2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample SmartMeter data
        smartMeter1 = new SmartMeter();
        smartMeter1.setId("1");
        smartMeter1.setUserId("user1");
        smartMeter1.setActive(true);

        smartMeter2 = new SmartMeter();
        smartMeter2.setId("2");
        smartMeter2.setUserId("user2");
        smartMeter2.setActive(true);
    }

    @Test
    void getSmartMeters_ShouldReturnListOfSmartMeters_WhenUserIdIsProvided() {
        String userId = "user1";
        List<SmartMeter> smartMeters = Arrays.asList(smartMeter1);

        when(smartMeterService.getSmartMetersByUser(userId)).thenReturn(smartMeters);

        List<SmartMeter> result = smartMeterController.getSmartMeters(userId);

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        verify(smartMeterService, times(1)).getSmartMetersByUser(userId);
    }

    @Test
    void getAllSmartMeters_ShouldReturnListOfAllSmartMeters() {
        List<SmartMeter> smartMeters = Arrays.asList(smartMeter1, smartMeter2);

        when(smartMeterService.getAllSmartMeters()).thenReturn(smartMeters);

        List<SmartMeter> result = smartMeterController.getSmartMeters();

        assertEquals(2, result.size());
        verify(smartMeterService, times(1)).getAllSmartMeters();
    }

    @Test
    void addSmartMeter_ShouldReturnSmartMeter_WhenAddedSuccessfully() {
        when(smartMeterService.addSmartMeter(smartMeter1)).thenReturn(smartMeter1);

        SmartMeter result = smartMeterController.addSmartMeter(smartMeter1);

        assertEquals("1", result.getId());
        verify(smartMeterService, times(1)).addSmartMeter(smartMeter1);
    }
}
