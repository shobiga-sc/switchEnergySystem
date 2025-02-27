package com.trustrace.switchEnergySystem.dto;


public class ProviderRateDTO {
    private String providerName;
    private double ratePerKWH;

    public ProviderRateDTO(String providerName, double ratePerKWH) {
        this.providerName = providerName;
        this.ratePerKWH = ratePerKWH;
    }

    // Getters and Setters
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public double getRatePerKWH() {
        return ratePerKWH;
    }

    public void setRatePerKWH(double ratePerKWH) {
        this.ratePerKWH = ratePerKWH;
    }
}
