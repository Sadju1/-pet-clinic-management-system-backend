package com.petclinic.service;

import com.petclinic.model.Pet;
import com.petclinic.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Optional<Pet> findById(UUID id) {
        return petRepository.findById(id);
    }

    public Pet update(UUID id, Pet pet) {
        pet.setId(id);
        return petRepository.save(pet);
    }

    public boolean existsById(UUID id) {
        return petRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        petRepository.deleteById(id);
    }

    public List<Pet> findBySpecies(String species) {
        return petRepository.findBySpeciesIgnoreCaseOrderByPetNameAsc(species);
    }

    public List<Pet> findByVeterinarianId(UUID veterinarianId) {
        return petRepository.findDistinctPetsByVeterinarianId(veterinarianId);
    }
}
