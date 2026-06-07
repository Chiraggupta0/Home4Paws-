package com.home4paws.home4paws.repository;

import com.home4paws.home4paws.model.Pet;
import com.home4paws.home4paws.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // find all pets belonging to a specific shelter
    List<Pet> findByShelterId(Long shelterId);

    // find all pets by status (AVAILABLE, ADOPTED, PENDING)
    List<Pet> findByStatus(String status);

    // find all pets of a specific species
    List<Pet> findBySpecies(String species);

    // find all available pets of a specific species
    List<Pet> findBySpeciesAndStatus(String species, String status);

    List<Pet> findByShelterEmail(String email);
}