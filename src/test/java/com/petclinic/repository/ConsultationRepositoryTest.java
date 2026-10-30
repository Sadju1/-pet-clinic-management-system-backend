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

/**
 * Runs against the real (already-seeded) PostgreSQL database — see application.properties.
 * Every assertion filters results down to this test's own "RepoTest-" tagged reasons before
 * checking exact content/order, so pre-existing Step 0 seed data never affects the outcome.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultationRepositoryTest {

    private static final String MARKER = "RepoTest-";

    @Autowired
    private ConsultationRepository consultationRepository;

    @Test
    void findByStatusOrderByVisitDateAsc_returnsMatchingStatusSortedByDate() {
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 15), ConsultationStatus.BOOKED, MARKER + "Checkup A"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 5), ConsultationStatus.BOOKED, MARKER + "Checkup B"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 10), ConsultationStatus.MISSED, MARKER + "No-show"));

        List<Consultation> booked = consultationRepository.findByStatusOrderByVisitDateAsc(ConsultationStatus.BOOKED);

        assertThat(booked).allMatch(c -> c.getStatus() == ConsultationStatus.BOOKED);
        assertThat(ourReasonsInOrder(booked)).containsExactly(MARKER + "Checkup B", MARKER + "Checkup A");
    }

    @Test
    void findByStatusOrderByVisitDateAsc_excludesOtherStatuses() {
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 1), ConsultationStatus.BOOKED, MARKER + "Isolation-Booked"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 1), ConsultationStatus.MISSED, MARKER + "Isolation-Missed"));

        List<Consultation> arrived = consultationRepository.findByStatusOrderByVisitDateAsc(ConsultationStatus.ARRIVED);

        assertThat(ourReasonsInOrder(arrived)).isEmpty();
    }

    @Test
    void findByVisitDateBetweenOrderByVisitDateAsc_isInclusiveOnBothEndsAndSorted() {
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 1), ConsultationStatus.BOOKED, MARKER + "Start boundary"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 31), ConsultationStatus.BOOKED, MARKER + "End boundary"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 10, 15), ConsultationStatus.BOOKED, MARKER + "Middle"));
        consultationRepository.save(newConsultation(LocalDate.of(2026, 11, 1), ConsultationStatus.BOOKED, MARKER + "Outside range"));

        List<Consultation> result = consultationRepository.findByVisitDateBetweenOrderByVisitDateAsc(
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));

        assertThat(ourReasonsInOrder(result))
                .containsExactly(MARKER + "Start boundary", MARKER + "Middle", MARKER + "End boundary");
    }

    private List<String> ourReasonsInOrder(List<Consultation> consultations) {
        return consultations.stream()
                .map(Consultation::getReason)
                .filter(reason -> reason != null && reason.startsWith(MARKER))
                .toList();
    }

    private Consultation newConsultation(LocalDate visitDate, ConsultationStatus status, String reason) {
        Consultation consultation = new Consultation();
        consultation.setVisitDate(visitDate);
        consultation.setStatus(status);
        consultation.setReason(reason);
        return consultation;
    }
}
