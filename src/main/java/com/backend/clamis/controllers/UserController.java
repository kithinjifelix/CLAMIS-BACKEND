package com.backend.clamis.controllers;

import com.backend.clamis.exception.ResourceNotFoundException;
import com.backend.clamis.model.Role;
import com.backend.clamis.model.User;
import com.backend.clamis.payload.request.LoginRequest;
import com.backend.clamis.payload.request.UpdateUserRequest;
import com.backend.clamis.payload.response.JwtResponse;
import com.backend.clamis.payload.response.MessageResponse;
import com.backend.clamis.repository.OrganisationRepository;
import com.backend.clamis.repository.RoleRepository;
import com.backend.clamis.repository.UserRepository;
import com.backend.clamis.payload.request.RegisterRequest;
import com.backend.clamis.security.jwt.JwtUtils;
import com.backend.clamis.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

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

    @GetMapping("/get/{userId}")
    public Optional<User> getUser(@PathVariable Long userId) {
        return userRepository.findById(userId);
    }

    @GetMapping("/getByOrganization/{organisationId}")
    public List<User> findByOrganisationId(@PathVariable Long organisationId) {
        return userRepository.findByOrganisationId(organisationId);
    }

    @PutMapping("/put/{userId}")
    public User updateUser(@PathVariable Long userId,
                           @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(updateUserRequest.getFirstName());
                    user.setMiddleName(updateUserRequest.getMiddleName());
                    user.setLastName(updateUserRequest.getLastName());
                    user.setUsername(updateUserRequest.getUsername());
                    user.setEmail(updateUserRequest.getEmail());
                    user.setPhone(updateUserRequest.getPhone());

                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
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

    @PostMapping("signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                                                userDetails.getId(),
                                                userDetails.getUsername(),
                                                userDetails.getEmail(),
                                                userDetails.getName(),
                                                roles));
    }
}
