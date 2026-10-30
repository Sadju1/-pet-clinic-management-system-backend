package com.petclinic.service;

import com.petclinic.model.Veterinarian;
import com.petclinic.repository.VeterinarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VeterinarianService {

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    public Veterinarian save(Veterinarian veterinarian) {
        return veterinarianRepository.save(veterinarian);
    }

    public List<Veterinarian> findAll() {
        return veterinarianRepository.findAll();
    }

    public Optional<Veterinarian> findById(UUID id) {
        return veterinarianRepository.findById(id);
    }

    public Veterinarian update(UUID id, Veterinarian veterinarian) {
        veterinarian.setId(id);
        return veterinarianRepository.save(veterinarian);
    }

    public boolean existsById(UUID id) {
        return veterinarianRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        veterinarianRepository.deleteById(id);
    }
}
