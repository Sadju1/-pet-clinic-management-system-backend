package com.petclinic.service;

import com.petclinic.model.Consultation;
import com.petclinic.model.ConsultationStatus;
import com.petclinic.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
