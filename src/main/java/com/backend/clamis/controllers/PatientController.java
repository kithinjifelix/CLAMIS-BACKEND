package com.backend.clamis.controllers;

import com.backend.clamis.model.Patient;
import com.backend.clamis.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/get")
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }
}
