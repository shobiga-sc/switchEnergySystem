package com.trustrace.switchEnergySystem.repository;



import com.trustrace.switchEnergySystem.entity.Role;
import com.trustrace.switchEnergySystem.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}
