package com.petclinic.repository;

import com.petclinic.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    List<Pet> findBySpeciesIgnoreCaseOrderByPetNameAsc(String species);

    @Query("SELECT DISTINCT c.pet FROM Consultation c WHERE c.veterinarian.id = :veterinarianId")
    List<Pet> findDistinctPetsByVeterinarianId(@Param("veterinarianId") UUID veterinarianId);
}
