package com.petclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.UUID;

@Entity
public class Veterinarian {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    private String licenseNumber;

    @ManyToOne
    @JoinColumn(name = "exam_room_id", nullable = true)
    private ExamRoom examRoom;

    @ManyToMany
    @JoinTable(
            name = "veterinarian_expertise",
            joinColumns = @JoinColumn(name = "veterinarian_id"),
            inverseJoinColumns = @JoinColumn(name = "expertise_id")
    )
    private List<Expertise> expertises;

    public Veterinarian() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public ExamRoom getExamRoom() {
        return examRoom;
    }

    public void setExamRoom(ExamRoom examRoom) {
        this.examRoom = examRoom;
    }

    public List<Expertise> getExpertises() {
        return expertises;
    }

    public void setExpertises(List<Expertise> expertises) {
        this.expertises = expertises;
    }
}
