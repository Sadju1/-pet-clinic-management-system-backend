package com.petclinic.repository;

import com.petclinic.model.Expertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpertiseRepository extends JpaRepository<Expertise, UUID> {

    @Query("SELECT e FROM Expertise e WHERE e.veterinarians IS EMPTY")
    List<Expertise> findUnusedExpertises();
}
