package com.petclinic.controller;

import com.petclinic.model.Expertise;
import com.petclinic.service.ExpertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/expertises")
public class ExpertiseController {

    @Autowired
    private ExpertiseService expertiseService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Expertise expertise) {
        Expertise saved = expertiseService.save(expertise);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Expertise saved successfully");
        response.put("data", saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Expertise>> getAll() {
        return ResponseEntity.ok(expertiseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Expertise> expertise = expertiseService.findById(id);
        if (expertise.isPresent()) {
            return ResponseEntity.ok(expertise.get());
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "The expertise with that id does not exist");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID id, @RequestBody Expertise expertise) {
        if (!expertiseService.existsById(id)) {
            Map<String, Object> notFound = new HashMap<>();
            notFound.put("message", "The expertise with that id does not exist");
            return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
        }
        Expertise updated = expertiseService.update(id, expertise);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Expertise updated successfully");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        Map<String, String> response = new HashMap<>();
        if (!expertiseService.existsById(id)) {
            response.put("message", "The expertise with that id does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        expertiseService.deleteById(id);
        response.put("message", "Expertise deleted successfully");
        return ResponseEntity.ok(response);
    }
}
