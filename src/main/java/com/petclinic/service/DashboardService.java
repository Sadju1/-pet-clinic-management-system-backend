package com.petclinic.service;

import com.petclinic.model.ConsultationStatus;
import com.petclinic.repository.ConsultationRepository;
import com.petclinic.repository.ExamRoomRepository;
import com.petclinic.repository.MedicalRecordRepository;
import com.petclinic.repository.PetRepository;
import com.petclinic.repository.VeterinarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private ExamRoomRepository examRoomRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("pets", petRepository.count());
        summary.put("veterinarians", veterinarianRepository.count());
        summary.put("freeExamRooms", (long) examRoomRepository.findFreeRooms().size());
        summary.put("consultationsToday", consultationRepository.countByVisitDate(LocalDate.now()));
        summary.put("booked", consultationRepository.countByStatus(ConsultationStatus.BOOKED));
        summary.put("medicalRecords", medicalRecordRepository.count());
        return summary;
    }
}
