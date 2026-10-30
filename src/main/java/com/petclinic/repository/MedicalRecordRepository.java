package com.petclinic.repository;

import com.petclinic.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    boolean existsByConsultationId(UUID consultationId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.consultation.pet.tagNumber = :tagNumber ORDER BY m.recordedDate DESC")
    List<MedicalRecord> findByPetTagNumber(@Param("tagNumber") String tagNumber);
}
