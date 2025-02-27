package com.trustrace.switchEnergySystem.controller;


import ch.qos.logback.classic.encoder.JsonEncoder;
import com.trustrace.switchEnergySystem.dto.CostSimulationDTO;
import com.trustrace.switchEnergySystem.dto.ProviderRateDTO;
import com.trustrace.switchEnergySystem.dto.UserDto;
import com.trustrace.switchEnergySystem.entity.Role;
import com.trustrace.switchEnergySystem.entity.SmartMeter;
import com.trustrace.switchEnergySystem.entity.User;

import com.trustrace.switchEnergySystem.repository.RoleRepository;
import com.trustrace.switchEnergySystem.repository.UserRepository;
import com.trustrace.switchEnergySystem.service.AuthService;
import com.trustrace.switchEnergySystem.service.ProviderService;
import com.trustrace.switchEnergySystem.service.SmartMeterService;
import com.trustrace.switchEnergySystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ROLE_ENDUSER')")
public class UserController {
    @Autowired
    private AuthService authService;
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Public access granted");
    }
    @GetMapping("/test")

    public ResponseEntity<String> testAccess(Authentication authentication) {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        return ResponseEntity.ok("Access granted for user: " + authentication.getName() + " with roles: " + authorities);
    }
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProviderService providerService;
    private final SmartMeterService smartMeterService;
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    public UserController(UserService userService, UserRepository userRepository, ProviderService providerService, SmartMeterService smartMeterService, AuthService authService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.providerService = providerService;
        this.smartMeterService = smartMeterService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable String userId) {
        logger.info("Request to show user details with the ID {} ",userId);
        return userService.getUserById(userId);
    }

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        logger.info("Request to add user");
        return userService.addUser(user);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable String userId) {
        logger.info("Request to detete the user with the ID {} ",userId);
        userService.deleteUserById(userId);
        logger.info("Deleted the user with the ID {} ",userId);
    }

    // Simulate costs based on the smart meter and the period (day, week, month)
    @GetMapping("/simulate-cost-all/{smartMeterId}")
    public List<CostSimulationDTO> simulateCostAll(@PathVariable String smartMeterId, @RequestParam String period) {
        LocalDateTime from = getStartOfPeriod(period);
        LocalDateTime to = LocalDateTime.now();
        logger.info("Request to show the smart meter's simulate-cost for all providers with the smt ID {} ",smartMeterId);
        return userService.simulateCostsAll(smartMeterId, from, to);
    }
    @GetMapping("/smartMeter/{userId}")
    public List<SmartMeter> getSmartMeters(@PathVariable String userId) {
        logger.info("Request to show the smart meter's  with the user ID {} ",userId);
        return smartMeterService.getSmartMetersByUser(userId);
    }

    @GetMapping("/simulate-cost/{smartMeterId}")
    public CostSimulationDTO simulateCost(@PathVariable String smartMeterId, @RequestParam String period) {
        logger.info("Request to show the smart meter's simulate-cost with the ID {} ",smartMeterId);
        LocalDateTime from = getStartOfPeriod(period);
        LocalDateTime to = LocalDateTime.now();
        return userService.simulateCosts(smartMeterId, from, to);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        logger.info("Request to create a new User");
        try {
            User newUser = new User();
            newUser.setUsername(userDto.getUsername());
            newUser.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

            // Handle role assignment based on Role class
            Set<Role> roles = new HashSet<>();
            for (String roleName : userDto.getRoles()) {
                // Assuming you have a method in your Role repository to find by name
                Role role = roleRepository.findByName(roleName) // Fetch the role from the database
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)); // Handle case where role is not found
                roles.add(role);
            }
            newUser.setRoles(roles);
            authService.saveUser(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
        }
    }

    // Helper method to get the start of the specified period
    private LocalDateTime getStartOfPeriod(String period) {
        switch (period.toLowerCase()) {
            case "day":
                return LocalDateTime.now().minusDays(1);
            case "week":
                return LocalDateTime.now().minusWeeks(1);
            case "month":
                return LocalDateTime.now().minusMonths(1);
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
    }

    @GetMapping("/providerCurrentRates")
    public List<ProviderRateDTO> getAllCurrentRates() {
        logger.info("Request to view all active energy providers and their current rates.");

        return providerService.getAllProvidersWithRates();
    }


}


