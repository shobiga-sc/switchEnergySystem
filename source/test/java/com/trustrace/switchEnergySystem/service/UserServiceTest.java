package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.dto.CostSimulationDTO;
import com.trustrace.switchEnergySystem.entity.Provider;
import com.trustrace.switchEnergySystem.entity.User;
import com.trustrace.switchEnergySystem.entity.UserSmartMeter;
import com.trustrace.switchEnergySystem.repository.ProviderRepository;
import com.trustrace.switchEnergySystem.repository.UserRepository;
import com.trustrace.switchEnergySystem.repository.UserSmartMeterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private BillingService billingService;

    @Mock
    private UserSmartMeterRepository userSmartMeterRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("password");

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.addUser(user);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId("1");
        user.setUsername("testUser");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById("1");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository).findById("1");
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("testUser1");
        User user2 = new User();
        user2.setUsername("testUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testDeleteUserById() {
        String userId = "1";
        userService.deleteUserById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testSimulateCostsAll() {
        String smartMeterId = "smartMeter1";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        Provider provider1 = new Provider();
        provider1.setId("provider1");
        provider1.setProviderName("Provider One");
        provider1.setRatePerKWH(0.10);

        Provider provider2 = new Provider();
        provider2.setId("provider2");
        provider2.setProviderName("Provider Two");
        provider2.setRatePerKWH(0.15);

        when(providerRepository.findAll()).thenReturn(Arrays.asList(provider1, provider2));
        when(billingService.calculateCost(smartMeterId, provider1.getId(), from, to)).thenReturn(5.0);
        when(billingService.calculateCost(smartMeterId, provider2.getId(), from, to)).thenReturn(7.5);

        List<CostSimulationDTO> simulations = userService.simulateCostsAll(smartMeterId, from, to);

        assertEquals(2, simulations.size());
        assertEquals("Provider One", simulations.get(0).getProviderName());
        assertEquals(5.0, simulations.get(0).getSimulatedCost());
        assertEquals("Provider Two", simulations.get(1).getProviderName());
        assertEquals(7.5, simulations.get(1).getSimulatedCost());
    }

    @Test
    void testSimulateCosts_ValidSmartMeter() {
        String smartMeterId = "smartMeter1";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        UserSmartMeter userSmartMeter = new UserSmartMeter();
        userSmartMeter.setSmartMeterId(smartMeterId);
        userSmartMeter.setProviderId("provider1");

        Provider provider = new Provider();
        provider.setId("provider1");
        provider.setProviderName("Provider One");
        provider.setRatePerKWH(0.10);
        provider.setActive(true);

        when(userSmartMeterRepository.findBySmartMeterId(smartMeterId)).thenReturn(userSmartMeter);
        when(providerRepository.findById("provider1")).thenReturn(Optional.of(provider));
        when(billingService.calculateCost(smartMeterId, provider.getId(), from, to)).thenReturn(5.0);

        CostSimulationDTO costSimulation = userService.simulateCosts(smartMeterId, from, to);

        assertEquals("Provider One", costSimulation.getProviderName());
        assertEquals(5.0, costSimulation.getSimulatedCost());
    }

    @Test
    void testSimulateCosts_InvalidSmartMeter() {
        String smartMeterId = "invalidSmartMeterId";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        when(userSmartMeterRepository.findBySmartMeterId(smartMeterId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.simulateCosts(smartMeterId, from, to);
        });

        assertEquals("Smart meter not found for ID: " + smartMeterId, exception.getMessage());
    }

    @Test
    void testSimulateCosts_InvalidProvider() {
        String smartMeterId = "smartMeter1";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        UserSmartMeter userSmartMeter = new UserSmartMeter();
        userSmartMeter.setSmartMeterId(smartMeterId);
        userSmartMeter.setProviderId("provider1");

        when(userSmartMeterRepository.findBySmartMeterId(smartMeterId)).thenReturn(userSmartMeter);
        when(providerRepository.findById("provider1")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.simulateCosts(smartMeterId, from, to);
        });

        assertEquals("Provider not found for ID: provider1", exception.getMessage());
    }
}
