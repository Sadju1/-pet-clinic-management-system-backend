package com.petclinic.repository;

import com.petclinic.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, UUID> {

    @Query("SELECT DISTINCT v FROM Veterinarian v JOIN v.expertises e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Veterinarian> findByExpertiseNameContaining(@Param("name") String name);

    boolean existsByExamRoomIdAndIdNot(UUID examRoomId, UUID id);
}
