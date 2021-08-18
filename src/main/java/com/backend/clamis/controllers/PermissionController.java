package com.backend.clamis.controllers;

import com.backend.clamis.model.Organisation;
import com.backend.clamis.model.Permission;
import com.backend.clamis.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {
    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/get")
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }
}
