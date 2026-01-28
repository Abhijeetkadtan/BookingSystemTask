package com.example.booking.dto;

import com.example.booking.entity.Role;
import lombok.Generated;

public class RegisterRequest {
    private String username;
    private String password;
    private Role role;

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public Role getRole() {
        return this.role;
    }

    @Generated
    public void setUsername(final String username) {
        this.username = username;
    }

    @Generated
    public void setPassword(final String password) {
        this.password = password;
    }

    @Generated
    public void setRole(final Role role) {
        this.role = role;
    }

    @Generated
    public RegisterRequest() {
    }
}
