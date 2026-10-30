package com.petclinic.controller;

import com.petclinic.model.Consultation;
import com.petclinic.model.ConsultationStatus;
import com.petclinic.model.MedicalRecord;
import com.petclinic.service.ConsultationService;
import com.petclinic.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam UUID consultationId, @RequestBody MedicalRecord medicalRecord) {
        Map<String, Object> response = new HashMap<>();
        Optional<Consultation> consultationOpt = consultationService.findById(consultationId);
        if (consultationOpt.isEmpty()) {
            response.put("message", "The consultation with that id does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Consultation consultation = consultationOpt.get();
        if (consultation.getStatus() != ConsultationStatus.TREATED) {
            response.put("message", "Medical records are only for TREATED consultations");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        medicalRecord.setConsultation(consultation);
        medicalRecord.setRecordedDate(LocalDate.now());
        MedicalRecord saved = medicalRecordService.save(medicalRecord);
        response.put("message", "Medical record saved successfully");
        response.put("data", saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/pet/{tagNumber}")
    public ResponseEntity<List<MedicalRecord>> getByPetTagNumber(@PathVariable String tagNumber) {
        return ResponseEntity.ok(medicalRecordService.findByPetTagNumber(tagNumber));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        Map<String, String> response = new HashMap<>();
        if (!medicalRecordService.existsById(id)) {
            response.put("message", "The medical record with that id does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        medicalRecordService.deleteById(id);
        response.put("message", "Medical record deleted successfully");
        return ResponseEntity.ok(response);
    }
}
