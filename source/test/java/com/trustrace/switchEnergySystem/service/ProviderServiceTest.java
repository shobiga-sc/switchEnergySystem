package com.trustrace.switchEnergySystem.service;


import com.trustrace.switchEnergySystem.dto.ProviderRateDTO;
import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProviderService providerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProviders() {
        // Given
        Provider provider1 = new Provider();
        provider1.setProviderName("Provider1");
        Provider provider2 = new Provider();
        provider2.setProviderName("Provider2");

        when(providerRepository.findAll()).thenReturn(Arrays.asList(provider1, provider2));

        // When
        List<Provider> result = providerService.getAllProviders();

        // Then
        assertEquals(2, result.size());
        assertEquals("Provider1", result.get(0).getProviderName());
        assertEquals("Provider2", result.get(1).getProviderName());
        verify(providerRepository, times(1)).findAll();
    }

    @Test
    void testGetActiveProviders() {
        // Given
        Provider provider1 = new Provider();
        provider1.setProviderName("Provider1");
        provider1.setActive(true);
        Provider provider2 = new Provider();
        provider2.setProviderName("Provider2");
        provider2.setActive(true);

        when(providerRepository.findByIsActive(true)).thenReturn(Arrays.asList(provider1, provider2));

        // When
        List<Provider> result = providerService.getActiveProviders();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.get(0).isActive());
        assertTrue(result.get(1).isActive());
        verify(providerRepository, times(1)).findByIsActive(true);
    }

    @Test
    void testAddProvider() {
        // Given
        Provider provider = new Provider();
        provider.setProviderName("Provider1");
        provider.setActive(true);

        when(providerRepository.save(provider)).thenReturn(provider);

        // When
        Provider result = providerService.addProvider(provider);

        // Then
        assertNotNull(result);
        assertEquals("Provider1", result.getProviderName());
        assertTrue(result.isActive());
        verify(providerRepository, times(1)).save(provider);
    }

    @Test
    void testDisableProvider() {
        // Given
        Provider provider = new Provider();
        provider.setProviderName("Provider1");
        provider.setActive(true);

        when(providerRepository.findById("providerId")).thenReturn(Optional.of(provider));
        when(providerRepository.save(provider)).thenReturn(provider);

        // When
        Provider result = providerService.disableProvider("providerId");

        // Then
        assertFalse(result.isActive());
        verify(providerRepository, times(1)).findById("providerId");
        verify(providerRepository, times(1)).save(provider);
    }

    @Test
    void testGetAllProvidersWithRates() {
        // Given
        Provider provider1 = new Provider();
        provider1.setProviderName("Provider1");
        provider1.setRatePerKWH(10.5);
        provider1.setActive(true);

        Provider provider2 = new Provider();
        provider2.setProviderName("Provider2");
        provider2.setRatePerKWH(12.3);
        provider2.setActive(true);

        when(providerRepository.findByIsActive(true)).thenReturn(Arrays.asList(provider1, provider2));

        // When
        List<ProviderRateDTO> result = providerService.getAllProvidersWithRates();

        // Then
        assertEquals(2, result.size());
        assertEquals("Provider1", result.get(0).getProviderName());
        assertEquals(10.5, result.get(0).getRatePerKWH());
        assertEquals("Provider2", result.get(1).getProviderName());
        assertEquals(12.3, result.get(1).getRatePerKWH());
        verify(providerRepository, times(1)).findByIsActive(true);
    }
}
