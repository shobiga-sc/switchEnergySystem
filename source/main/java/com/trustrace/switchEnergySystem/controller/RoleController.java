package com.trustrace.switchEnergySystem.controller;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.trustrace.switchEnergySystem.entity.Role;
import com.trustrace.switchEnergySystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    @PostMapping("/create")
    public Role createRole(@RequestBody Role role) {
        logger.info("Adding a new role : {}", role);
        return roleRepository.save(role);
    }
}
