package com.petclinic.controller;

import com.petclinic.model.Consultation;
import com.petclinic.model.ConsultationStatus;
import com.petclinic.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Consultation consultation) {
        Consultation saved = consultationService.save(consultation);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation saved successfully");
        response.put("data", saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Consultation>> getAll() {
        return ResponseEntity.ok(consultationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Consultation> consultation = consultationService.findById(id);
        if (consultation.isPresent()) {
            return ResponseEntity.ok(consultation.get());
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "The consultation with that id does not exist");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID id, @RequestBody Consultation consultation) {
        if (!consultationService.existsById(id)) {
            Map<String, Object> notFound = new HashMap<>();
            notFound.put("message", "The consultation with that id does not exist");
            return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
        }
        Consultation updated = consultationService.update(id, consultation);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation updated successfully");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        Map<String, String> response = new HashMap<>();
        if (!consultationService.existsById(id)) {
            response.put("message", "The consultation with that id does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        consultationService.deleteById(id);
        response.put("message", "Consultation deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<Consultation>> getByStatus(@RequestParam ConsultationStatus status) {
        return ResponseEntity.ok(consultationService.findByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<Consultation>> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(consultationService.findByVisitDateBetween(startDate, endDate));
    }
}
