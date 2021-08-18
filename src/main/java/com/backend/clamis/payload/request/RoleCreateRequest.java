package com.backend.clamis.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RoleCreateRequest {
    @NotBlank
    @Size(max = 20)
    private String name;

    private List<Long> rolePermissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getRolePermissions() {
        return this.rolePermissions;
    }

    public void setRolePermissions(List<Long> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }
}
