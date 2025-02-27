package com.trustrace.switchEnergySystem.dto;

public class CostSimulationDTO {
    private String providerName;
    private double simulatedCost;

    public CostSimulationDTO(String providerName, double simulatedCost) {
        this.providerName = providerName;
        this.simulatedCost = simulatedCost;
    }

    // Getters and setters

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public double getSimulatedCost() {
        return simulatedCost;
    }

    public void setSimulatedCost(double simulatedCost) {
        this.simulatedCost = simulatedCost;
    }
}
