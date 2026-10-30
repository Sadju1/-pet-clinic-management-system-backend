package com.petclinic.repository;

import com.petclinic.model.Expertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ExpertiseRepository extends JpaRepository<Expertise, UUID> {
}
