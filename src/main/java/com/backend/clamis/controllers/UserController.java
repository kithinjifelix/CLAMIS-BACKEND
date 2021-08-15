package com.backend.clamis.controllers;

import com.backend.clamis.exception.ResourceNotFoundException;
import com.backend.clamis.model.Role;
import com.backend.clamis.model.User;
import com.backend.clamis.payload.response.MessageResponse;
import com.backend.clamis.repository.OrganisationRepository;
import com.backend.clamis.repository.RoleRepository;
import com.backend.clamis.repository.UserRepository;
import com.backend.clamis.payload.request.RegisterRequest;
import com.backend.clamis.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/get")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getByOrganization/{organisationId}")
    public List<User> findByOrganisationId(@PathVariable Long organisationId) {
        return userRepository.findByOrganisationId(organisationId);
    }

    @PostMapping("/create/{organisationId}/{roleId}")
    public ResponseEntity<?> addUser(@PathVariable Long organisationId, @PathVariable Long roleId,
                        @Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getFirstName(),
                registerRequest.getMiddleName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                registerRequest.getPhone(),
                encoder.encode(registerRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role selectedRole = roleRepository.getById(roleId);
        roles.add(selectedRole);

        organisationRepository.findById(organisationId)
                .map(organisation -> {
                    user.setOrganisation(organisation);
                    user.setRoles(roles);
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("Organisation not found with id " + organisationId));

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
