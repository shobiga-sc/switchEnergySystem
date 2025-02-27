package com.trustrace.switchEnergySystem.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "readings")
public class Reading {

    @Id
    private String id;

    @NotBlank(message = "Smart meter ID is required")
    private String smartMeterId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Kilowatt reading must be greater than 0")
    private double kilowatt;  // kW reading

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;  // Timestamp of the reading

    // Constructor that matches the fields
    public Reading(String smartMeterId, double kilowatt, LocalDateTime timestamp) {
        this.smartMeterId = smartMeterId;
        this.kilowatt = kilowatt;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getSmartMeterId() {
        return smartMeterId;
    }

    public void setSmartMeterId(String smartMeterId) {
        this.smartMeterId = smartMeterId;
    }

    public double getKilowatt() {
        return kilowatt;
    }

    public void setKilowatt(double kilowatt) {
        this.kilowatt = kilowatt;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
