package com.trustrace.switchEnergySystem.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Document(collection = "providerSwitchLogs")
public class ProviderSwitchLog {

    @Id
    private String id;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Smart Meter ID is required")
    private String smartMeterId;

    @NotBlank(message = "Old Provider ID is required")
    private String oldProviderId;

    @NotBlank(message = "New Provider ID is required")
    private String newProviderId;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date switchDate;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSmartMeterId() {
        return smartMeterId;
    }

    public void setSmartMeterId(String smartMeterId) {
        this.smartMeterId = smartMeterId;
    }

    public String getOldProviderId() {
        return oldProviderId;
    }

    public void setOldProviderId(String oldProviderId) {
        this.oldProviderId = oldProviderId;
    }

    public String getNewProviderId() {
        return newProviderId;
    }

    public void setNewProviderId(String newProviderId) {
        this.newProviderId = newProviderId;
    }

    public Date getSwitchDate() {
        return switchDate;
    }

    public void setSwitchDate(Date switchDate) {
        this.switchDate = switchDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
