package com.petclinic.repository;

import com.petclinic.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, UUID> {
}
