package com.home4paws.home4paws.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;        // Dog, Cat etc.

    private String breed;

    private Integer age;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetStatus status;         // AVAILABLE, ADOPTED, PENDING

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private User shelter;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = PetStatus.AVAILABLE;
        }
    }
//    automatically set created_at when shelter adds a dog

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public void setStatus(PetStatus status) {
        this.status = status;
    }
    public PetStatus getStatus() {
        return status;
    }

    public User getShelter() { return shelter; }
    public void setShelter(User shelter) { this.shelter = shelter; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}