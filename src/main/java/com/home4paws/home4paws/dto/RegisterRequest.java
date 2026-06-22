package com.home4paws.home4paws.dto;

import com.home4paws.home4paws.model.Role;

public class RegisterRequest {
    private String name;
    private Role role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
