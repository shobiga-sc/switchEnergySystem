package com.trustrace.switchEnergySystem.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Document(collection = "providers")
public class Provider {

    @Id
    private String id;

    @NotBlank(message = "Provider name is required")
    @Size(max = 100, message = "Provider name must be less than 100 characters")
    @Indexed(unique = true)
    private String providerName;

    @DecimalMin(value = "0.0", inclusive = false, message = "Rate per KWH must be greater than 0")
    private double ratePerKWH;

    @JsonProperty("active")  // Explicitly map JSON 'active' to 'isActive'
    private boolean isActive;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
    private String contactPhone;

    @NotEmpty(message = "Service areas cannot be empty")
    private List<@NotBlank(message = "Service area cannot be blank") String> serviceAreas;

    private Tariffs tariffs; // Nested object for tariffs

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<String> getServiceAreas() {
        return serviceAreas;
    }

    public void setServiceAreas(List<String> serviceAreas) {
        this.serviceAreas = serviceAreas;
    }

    public Tariffs getTariffs() {
        return tariffs;
    }

    public void setTariffs(Tariffs tariffs) {
        this.tariffs = tariffs;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Nested class for tariffs
    public static class Tariffs {

        @DecimalMin(value = "0.0", inclusive = false, message = "Daily rate must be greater than 0")
        private double dailyRate;

        @DecimalMin(value = "0.0", inclusive = false, message = "Weekly rate must be greater than 0")
        private double weeklyRate;

        @DecimalMin(value = "0.0", inclusive = false, message = "Monthly rate must be greater than 0")
        private double monthlyRate;

        public double getDailyRate() {
            return dailyRate;
        }

        public void setDailyRate(double dailyRate) {
            this.dailyRate = dailyRate;
        }

        public double getWeeklyRate() {
            return weeklyRate;
        }

        public void setWeeklyRate(double weeklyRate) {
            this.weeklyRate = weeklyRate;
        }

        public double getMonthlyRate() {
            return monthlyRate;
        }

        public void setMonthlyRate(double monthlyRate) {
            this.monthlyRate = monthlyRate;
        }
    }
}
