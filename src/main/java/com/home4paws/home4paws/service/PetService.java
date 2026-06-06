package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.Pet;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.PetRepository;
import com.home4paws.home4paws.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        pet.setStatus("AVAILABLE");

        return petRepository.save(pet);
    }

    // 2. GET ALL AVAILABLE PETS — anyone can see these

    public List<Pet> getAllAvailablePets() {
        return petRepository.findByStatus("AVAILABLE");
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
            throw new RuntimeException(
                    "You are not authorized to delete this pet");
        }

        petRepository.delete(pet);
    }
}