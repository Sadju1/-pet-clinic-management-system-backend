package com.petclinic.controller;

import com.petclinic.model.Veterinarian;
import com.petclinic.service.VeterinarianService;
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
@RequestMapping("/api/veterinarians")
public class VeterinarianController {

    @Autowired
    private VeterinarianService veterinarianService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Veterinarian veterinarian) {
        Veterinarian saved = veterinarianService.save(veterinarian);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Veterinarian saved successfully");
        response.put("data", saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Veterinarian>> getAll() {
        return ResponseEntity.ok(veterinarianService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Veterinarian> veterinarian = veterinarianService.findById(id);
        if (veterinarian.isPresent()) {
            return ResponseEntity.ok(veterinarian.get());
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "The veterinarian with that id does not exist");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID id, @RequestBody Veterinarian veterinarian) {
        if (!veterinarianService.existsById(id)) {
            Map<String, Object> notFound = new HashMap<>();
            notFound.put("message", "The veterinarian with that id does not exist");
            return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
        }
        Veterinarian updated = veterinarianService.update(id, veterinarian);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Veterinarian updated successfully");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        Map<String, String> response = new HashMap<>();
        if (!veterinarianService.existsById(id)) {
            response.put("message", "The veterinarian with that id does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        veterinarianService.deleteById(id);
        response.put("message", "Veterinarian deleted successfully");
        return ResponseEntity.ok(response);
    }
}
