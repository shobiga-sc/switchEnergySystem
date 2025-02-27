package com.trustrace.switchEnergySystem.service;

import com.trustrace.switchEnergySystem.entity.User;
import com.trustrace.switchEnergySystem.repository.UserRepository;
import com.trustrace.switchEnergySystem.repository.RoleRepository; // Make sure to import RoleRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trustrace.switchEnergySystem.dto.UserDto;
import com.trustrace.switchEnergySystem.entity.Role;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository; // Add RoleRepository to handle roles

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        // Create a Set for roles
        Set<Role> roles = new HashSet<>();

        // Fetch each role from the RoleRepository
        for (String roleName : userDto.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }

        user.setRoles(roles); // Set the roles
        return userRepository.save(user); // Save the user and return it
    }


    public boolean login(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if (user != null) {
            return passwordEncoder.matches(userDto.getPassword(), user.getPasswordHash());
        }
        return false;
    }

    public User saveUser(User user) {
        return userRepository.save(user); // Save the user to the database
    }
}
