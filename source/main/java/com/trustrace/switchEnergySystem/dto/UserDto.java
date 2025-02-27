package com.trustrace.switchEnergySystem.dto;

import java.util.Set;

public class UserDto {
    private String username;
    private String password;
    private Set<String> roles; // Set of role names as strings

    public UserDto() {
        // Default constructor
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
