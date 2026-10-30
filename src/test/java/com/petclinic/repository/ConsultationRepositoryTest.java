package com.petclinic.repository;

import com.petclinic.model.Consultation;
import com.petclinic.model.ConsultationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultationRepositoryTest {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Test
    void findByStatusOrderByVisitDateAsc_returnsMatchingStatusSortedByDate() {
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 15), ConsultationStatus.BOOKED, "Checkup A"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 5), ConsultationStatus.BOOKED, "Checkup B"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 10), ConsultationStatus.MISSED, "No-show"));

        List<Consultation> booked = consultationRepository.findByStatusOrderByVisitDateAsc(ConsultationStatus.BOOKED);

        assertThat(booked).extracting(Consultation::getReason).containsExactly("Checkup B", "Checkup A");
        assertThat(booked).allMatch(c -> c.getStatus() == ConsultationStatus.BOOKED);
    }

    @Test
    void findByStatusOrderByVisitDateAsc_returnsEmptyListWhenNoMatch() {
        List<Consultation> result = consultationRepository.findByStatusOrderByVisitDateAsc(ConsultationStatus.TREATED);

        assertThat(result).isEmpty();
    }

    @Test
    void findByVisitDateBetweenOrderByVisitDateAsc_isInclusiveOnBothEndsAndSorted() {
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 1), ConsultationStatus.BOOKED, "Start boundary"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 31), ConsultationStatus.BOOKED, "End boundary"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 15), ConsultationStatus.BOOKED, "Middle"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 11, 1), ConsultationStatus.BOOKED, "Outside range"));

        List<Consultation> result = consultationRepository.findByVisitDateBetweenOrderByVisitDateAsc(
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));

        assertThat(result).extracting(Consultation::getReason)
                .containsExactly("Start boundary", "Middle", "End boundary");
    }

    private Consultation newConsultation(LocalDate visitDate, ConsultationStatus status, String reason) {
        Consultation consultation = new Consultation();
        consultation.setVisitDate(visitDate);
        consultation.setStatus(status);
        consultation.setReason(reason);
        return consultation;
    }
}
