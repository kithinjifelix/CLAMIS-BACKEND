package com.backend.clamis.controllers;

import com.backend.clamis.exception.ResourceNotFoundException;
import com.backend.clamis.model.Role;
import com.backend.clamis.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/get")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/get/{roleId}")
    public Optional<Role> getRole(@PathVariable Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        return role;
    }

    @PostMapping("/create")
    public Role createRole(@Valid @RequestBody Role role) {
        return roleRepository.save(role);
    }

    @PutMapping("/put/{roleId}")
    public Role updateRole(@PathVariable Long roleId,
                           @Valid @RequestBody Role roleRequest) {
        return roleRepository.findById(roleId)
                .map(role -> {
                    role.setName(roleRequest.getName());

                    return roleRepository.save(role);
                }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));
    }
}
