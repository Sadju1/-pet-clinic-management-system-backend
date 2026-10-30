package com.petclinic.repository;

import com.petclinic.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
}
