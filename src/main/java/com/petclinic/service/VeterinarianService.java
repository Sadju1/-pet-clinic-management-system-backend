package com.petclinic.service;

import com.petclinic.model.Expertise;
import com.petclinic.model.Veterinarian;
import com.petclinic.repository.VeterinarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Veterinarian> findByExpertiseName(String name) {
        return veterinarianRepository.findByExpertiseNameContaining(name);
    }

    public boolean hasExpertise(Veterinarian veterinarian, UUID expertiseId) {
        return veterinarian.getExpertises() != null
                && veterinarian.getExpertises().stream().anyMatch(e -> e.getId().equals(expertiseId));
    }

    public Veterinarian addExpertise(Veterinarian veterinarian, Expertise expertise) {
        if (veterinarian.getExpertises() == null) {
            veterinarian.setExpertises(new ArrayList<>());
        }
        veterinarian.getExpertises().add(expertise);
        return veterinarianRepository.save(veterinarian);
    }

    public boolean isExamRoomTakenByAnother(UUID examRoomId, UUID veterinarianId) {
        return veterinarianRepository.existsByExamRoomIdAndIdNot(examRoomId, veterinarianId);
    }
}
