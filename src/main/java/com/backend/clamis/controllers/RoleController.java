package com.backend.clamis.controllers;

import com.backend.clamis.exception.ResourceNotFoundException;
import com.backend.clamis.model.Permission;
import com.backend.clamis.model.Role;
import com.backend.clamis.payload.request.RoleCreateRequest;
import com.backend.clamis.payload.response.MessageResponse;
import com.backend.clamis.repository.PermissionRepository;
import com.backend.clamis.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @GetMapping("/get")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/get/{roleId}")
    public Optional<Role> getRole(@PathVariable Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        return role;
    }

    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        try {
            roleRepository.deleteById(roleId);
            return ResponseEntity.ok(new MessageResponse("Role deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleCreateRequest roleCreateRequest) {
        try {
            Role role = new Role(roleCreateRequest.getName());

            Set<Permission> permissions = new HashSet<>();
            List<Long> setPermissions = roleCreateRequest.getRolePermissions();
            setPermissions.forEach(x -> {
                Permission permission = permissionRepository.findById(x).get();
                if (permission != null) {
                    permissions.add(permission);
                }
            });

            role.setPermissions(permissions);
            roleRepository.save(role);

            return ResponseEntity.ok(new MessageResponse("Role saved successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/put/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable Long roleId,
                           @Valid @RequestBody RoleCreateRequest roleRequest) {
        try {
            Set<Permission> permissions = new HashSet<>();
            List<Long> setPermissions = roleRequest.getRolePermissions();
            setPermissions.forEach(x -> {
                Permission permission = permissionRepository.findById(x).get();
                if (permission != null) {
                    permissions.add(permission);
                }
            });

            roleRepository.findById(roleId)
                    .map(role -> {
                        role.setName(roleRequest.getName());
                        role.setPermissions(permissions);
                        return roleRepository.save(role);
                    }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));

            return ResponseEntity.ok(new MessageResponse("Role saved successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
