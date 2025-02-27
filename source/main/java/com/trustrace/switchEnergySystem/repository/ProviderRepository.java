package com.trustrace.switchEnergySystem.repository;

import com.trustrace.switchEnergySystem.entity.Provider;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends MongoRepository<Provider, String> {
    List<Provider> findByIsActive(boolean isActive);

    Optional<Provider> findById(String providerId);
}
