package com.petclinic.service;

import com.petclinic.model.Expertise;
import com.petclinic.repository.ExpertiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpertiseService {

    @Autowired
    private ExpertiseRepository expertiseRepository;

    public Expertise save(Expertise expertise) {
        return expertiseRepository.save(expertise);
    }

    public List<Expertise> findAll() {
        return expertiseRepository.findAll();
    }

    public Optional<Expertise> findById(UUID id) {
        return expertiseRepository.findById(id);
    }

    public Expertise update(UUID id, Expertise expertise) {
        expertise.setId(id);
        return expertiseRepository.save(expertise);
    }

    public boolean existsById(UUID id) {
        return expertiseRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        expertiseRepository.deleteById(id);
    }

    public List<Expertise> findUnused() {
        return expertiseRepository.findUnusedExpertises();
    }
}
