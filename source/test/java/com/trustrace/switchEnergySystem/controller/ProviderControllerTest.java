package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.entity.SmartMeter;
import com.trustrace.switchEnergySystem.service.ProviderService;
import com.trustrace.switchEnergySystem.service.SmartMeterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProviderControllerTest {

    @InjectMocks
    private ProviderController providerController;

    @Mock
    private ProviderService providerService;

    @Mock
    private SmartMeterService smartMeterService;

    private Provider provider1;
    private Provider provider2;
    private SmartMeter smartMeter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample Provider data
        provider1 = new Provider();
        provider1.setId("1");
        provider1.setProviderName("Provider A");

        provider2 = new Provider();
        provider2.setId("2");
        provider2.setProviderName("Provider B");

        // Sample SmartMeter data
        smartMeter = new SmartMeter();
        smartMeter.setId("1");
        smartMeter.setUserId("user1");
        smartMeter.setActive(true);
    }

    @Test
    void getAllProviders_ShouldReturnListOfProviders() {
        List<Provider> providers = Arrays.asList(provider1, provider2);

        when(providerService.getAllProviders()).thenReturn(providers);

        List<Provider> result = providerController.getAllProviders();

        assertEquals(2, result.size());
        verify(providerService, times(1)).getAllProviders();
    }

    @Test
    void getActiveProviders_ShouldReturnListOfActiveProviders() {
        List<Provider> activeProviders = Arrays.asList(provider1);

        when(providerService.getActiveProviders()).thenReturn(activeProviders);

        List<Provider> result = providerController.getActiveProviders();

        assertEquals(1, result.size());
        verify(providerService, times(1)).getActiveProviders();
    }

    @Test
    void addProvider_ShouldReturnAddedProvider() {
        when(providerService.addProvider(provider1)).thenReturn(provider1);

        Provider result = providerController.addProvider(provider1);

        assertEquals("Provider A", result.getProviderName());
        verify(providerService, times(1)).addProvider(provider1);
    }

    @Test
    void disableProvider_ShouldReturnDisabledProvider() {
        when(providerService.disableProvider("1")).thenReturn(provider1);

        Provider result = providerController.disableProvider("1");

        assertEquals("Provider A", result.getProviderName());
        verify(providerService, times(1)).disableProvider("1");
    }

    @Test
    void decommissionSmartMeter_ShouldReturnDecommissionedSmartMeter() {
        when(smartMeterService.decommissionSmartMeter("1")).thenReturn(smartMeter);

        SmartMeter result = providerController.decommissionSmartMeter("1");

        assertEquals("1", result.getId());
        verify(smartMeterService, times(1)).decommissionSmartMeter("1");
    }


    @Test
    void testAccess_ShouldReturnAccessGrantedMessage() {
        // Mocking the Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList()); // Mock an empty list of authorities

        ResponseEntity<String> response = providerController.testAccess(authentication);

        // Adjusting the assertion to check for an empty roles list
        assertEquals("Access granted for user: testUser with roles: []", response.getBody());
    }
}
