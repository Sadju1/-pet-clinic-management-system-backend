package com.petclinic.repository;

import com.petclinic.model.Consultation;
import com.petclinic.model.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    List<Consultation> findByStatusOrderByVisitDateAsc(ConsultationStatus status);

    List<Consultation> findByVisitDateBetweenOrderByVisitDateAsc(LocalDate start, LocalDate end);

    boolean existsByVeterinarianIdAndVisitDateAndStatusNot(UUID veterinarianId, LocalDate visitDate, ConsultationStatus status);
}
