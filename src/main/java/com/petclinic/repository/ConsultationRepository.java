package com.petclinic.repository;

import com.petclinic.model.Consultation;
import com.petclinic.model.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    List<Consultation> findByStatusOrderByVisitDateAsc(ConsultationStatus status);

    List<Consultation> findByVisitDateBetweenOrderByVisitDateAsc(LocalDate start, LocalDate end);

    boolean existsByVeterinarianIdAndVisitDateAndStatusNot(UUID veterinarianId, LocalDate visitDate, ConsultationStatus status);

    long countByVisitDate(LocalDate visitDate);

    long countByStatus(ConsultationStatus status);

    @Query("SELECT c.status, COUNT(c) FROM Consultation c GROUP BY c.status")
    List<Object[]> countConsultationsByStatus();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Consultation c SET c.status = com.petclinic.model.ConsultationStatus.MISSED "
            + "WHERE c.veterinarian.id = :veterinarianId AND c.visitDate = :date "
            + "AND c.status <> com.petclinic.model.ConsultationStatus.TREATED")
    int cancelConsultationsForDay(@Param("veterinarianId") UUID veterinarianId, @Param("date") LocalDate date);
}
