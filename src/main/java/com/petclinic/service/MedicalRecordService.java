package com.petclinic.service;

import com.petclinic.model.MedicalRecord;
import com.petclinic.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord save(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public Optional<MedicalRecord> findById(UUID id) {
        return medicalRecordRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return medicalRecordRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        medicalRecordRepository.deleteById(id);
    }

    public boolean existsByConsultationId(UUID consultationId) {
        return medicalRecordRepository.existsByConsultationId(consultationId);
    }

    public List<MedicalRecord> findByPetTagNumber(String tagNumber) {
        return medicalRecordRepository.findByPetTagNumber(tagNumber);
    }
}
