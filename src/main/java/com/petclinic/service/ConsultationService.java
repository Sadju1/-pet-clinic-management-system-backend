package com.petclinic.service;

import com.petclinic.model.Consultation;
import com.petclinic.model.ConsultationStatus;
import com.petclinic.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    public Consultation save(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    public List<Consultation> findAll() {
        return consultationRepository.findAll();
    }

    public Optional<Consultation> findById(UUID id) {
        return consultationRepository.findById(id);
    }

    public Consultation update(UUID id, Consultation consultation) {
        consultation.setId(id);
        ConsultationStatus existingStatus = consultationRepository.findById(id)
                .map(Consultation::getStatus)
                .orElse(consultation.getStatus());
        consultation.setStatus(existingStatus);
        return consultationRepository.save(consultation);
    }

    public boolean existsById(UUID id) {
        return consultationRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        consultationRepository.deleteById(id);
    }

    public List<Consultation> findByStatus(ConsultationStatus status) {
        return consultationRepository.findByStatusOrderByVisitDateAsc(status);
    }

    public List<Consultation> findByVisitDateBetween(LocalDate start, LocalDate end) {
        return consultationRepository.findByVisitDateBetweenOrderByVisitDateAsc(start, end);
    }

    public boolean isDateInPast(LocalDate visitDate) {
        return visitDate != null && visitDate.isBefore(LocalDate.now());
    }

    public boolean hasConflictingConsultation(UUID veterinarianId, LocalDate visitDate) {
        return consultationRepository.existsByVeterinarianIdAndVisitDateAndStatusNot(
                veterinarianId, visitDate, ConsultationStatus.MISSED);
    }

    public boolean isValidStatusTransition(ConsultationStatus from, ConsultationStatus to) {
        if (from == ConsultationStatus.BOOKED) {
            return to == ConsultationStatus.ARRIVED || to == ConsultationStatus.MISSED;
        }
        if (from == ConsultationStatus.ARRIVED) {
            return to == ConsultationStatus.TREATED || to == ConsultationStatus.MISSED;
        }
        return false;
    }

    public Consultation updateStatus(Consultation consultation, ConsultationStatus newStatus) {
        consultation.setStatus(newStatus);
        return consultationRepository.save(consultation);
    }

    public List<Object[]> countConsultationsByStatus() {
        return consultationRepository.countConsultationsByStatus();
    }

    public int cancelConsultationsForDay(UUID veterinarianId, LocalDate date) {
        return consultationRepository.cancelConsultationsForDay(veterinarianId, date);
    }

    public Page<Consultation> findAll(Pageable pageable) {
        return consultationRepository.findAll(pageable);
    }
}
