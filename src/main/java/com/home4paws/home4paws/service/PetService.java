package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.Pet;
import com.home4paws.home4paws.model.PetStatus;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.PetRepository;
import com.home4paws.home4paws.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository,
                      UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    // 1. ADD PET — shelter adds a new pet

    public Pet addPet(Pet pet, String shelterEmail) {

        User shelter = userRepository.findByEmail(shelterEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Shelter not found: " + shelterEmail));

        pet.setShelter(shelter);

        // Set default status
        pet.setStatus(PetStatus.AVAILABLE);

        Pet saved = petRepository.save(pet);
        log.info("Pet added id={} name={} by shelter={}", saved.getId(), saved.getName(), shelterEmail);
        return saved;
    }

    // 2. GET ALL AVAILABLE PETS — anyone can see these

    public List<Pet> getAllAvailablePets() {
        return petRepository.findByStatus(PetStatus.AVAILABLE);
    }

    public List<Pet> getMyPets(String shelterEmail) {

        return petRepository.findByShelterEmail(
                shelterEmail
        );
    }

    // 3. GET PET BY ID — fetch single pet

    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Pet not found with id: " + id));
    }

    // 4. DELETE PET — shelter removes a pet

    public void deletePet(Long id, String shelterEmail) {

        // Find the pet
        Pet pet = getPetById(id);

        // Confirm the pet belongs to this shelter
        if (!pet.getShelter().getEmail().equals(shelterEmail)) {
            log.warn("Unauthorized delete attempt on pet id={} by {}", id, shelterEmail);
            throw new RuntimeException(
                    "You are not authorized to delete this pet");
        }

        petRepository.delete(pet);
        log.info("Pet deleted id={} by shelter={}", id, shelterEmail);
    }

}