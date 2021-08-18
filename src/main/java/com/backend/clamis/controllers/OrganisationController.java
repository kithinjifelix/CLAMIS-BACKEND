package com.backend.clamis.controllers;

import com.backend.clamis.exception.ResourceNotFoundException;
import com.backend.clamis.model.Organisation;
import com.backend.clamis.payload.response.MessageResponse;
import com.backend.clamis.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organisations")
@CrossOrigin(origins = "*")
public class OrganisationController {
    @Autowired
    private OrganisationRepository organisationRepository;

    @GetMapping("/get")
    public List<Organisation> getOrganisations() {
        return organisationRepository.findAll();
    }

    @GetMapping("/get/{organisationId}")
    public Optional<Organisation> getOrganisation(@PathVariable Long organisationId) {
        return organisationRepository.findById(organisationId);
    }

    @DeleteMapping("/delete/{organisationId}")
    public ResponseEntity<?> deleteOrganisation(@PathVariable Long organisationId) {
        try {
            organisationRepository.deleteById(organisationId);
            return ResponseEntity.ok(new MessageResponse("Organisation deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/create")
    public Organisation createOrganisation(@Valid @RequestBody Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    @PutMapping("/put/{organisationId}")
    public Organisation updateOrganisation(@PathVariable Long organisationId,
                                           @Valid @RequestBody Organisation organisationRequest) {
        return organisationRepository.findById(organisationId)
                .map(organisation -> {
                    organisation.setName(organisationRequest.getName());
                    organisation.setContact(organisationRequest.getContact());
                    organisation.setDescription(organisationRequest.getDescription());
                    organisation.setEmail(organisationRequest.getEmail());
                    organisation.setPhone(organisationRequest.getPhone());

                    return organisationRepository.save(organisation);
                }).orElseThrow(() -> new ResourceNotFoundException("Organisation not found with id " + organisationId));
    }
}
