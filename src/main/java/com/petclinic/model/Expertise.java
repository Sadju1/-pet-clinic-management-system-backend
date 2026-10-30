package com.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;
import java.util.UUID;

@Entity
public class Expertise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "expertises")
    @JsonIgnore
    private List<Veterinarian> veterinarians;

    public Expertise() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Veterinarian> getVeterinarians() {
        return veterinarians;
    }

    public void setVeterinarians(List<Veterinarian> veterinarians) {
        this.veterinarians = veterinarians;
    }
}
