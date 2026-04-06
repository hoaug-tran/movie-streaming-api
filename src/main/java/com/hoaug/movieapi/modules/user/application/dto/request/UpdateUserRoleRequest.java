package com.hoaug.movieapi.modules.user.application.dto.request;

import jakarta.validation.constraints.NotNull;
import com.hoaug.movieapi.modules.user.domain.model.Role;

public class UpdateUserRoleRequest {
    @NotNull
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
