package com.petclinic.controller;

import com.petclinic.model.Pet;
import com.petclinic.service.PetService;
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
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Pet pet) {
        Pet saved = petService.save(pet);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pet saved successfully");
        response.put("data", saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pet>> getAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Pet> pet = petService.findById(id);
        if (pet.isPresent()) {
            return ResponseEntity.ok(pet.get());
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "The pet with that id does not exist");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID id, @RequestBody Pet pet) {
        if (!petService.existsById(id)) {
            Map<String, Object> notFound = new HashMap<>();
            notFound.put("message", "The pet with that id does not exist");
            return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
        }
        Pet updated = petService.update(id, pet);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pet updated successfully");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        Map<String, String> response = new HashMap<>();
        if (!petService.existsById(id)) {
            response.put("message", "The pet with that id does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        petService.deleteById(id);
        response.put("message", "Pet deleted successfully");
        return ResponseEntity.ok(response);
    }
}
