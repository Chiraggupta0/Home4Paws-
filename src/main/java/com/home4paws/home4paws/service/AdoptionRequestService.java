package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.*;
import com.home4paws.home4paws.repository.AdoptionRequestRepository;
import com.home4paws.home4paws.repository.PetRepository;
import com.home4paws.home4paws.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.home4paws.home4paws.model.PetStatus;

import java.util.List;

@Service
public class AdoptionRequestService {

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public AdoptionRequestService(
            AdoptionRequestRepository adoptionRequestRepository,
            UserRepository userRepository,
            PetRepository petRepository) {

        this.adoptionRequestRepository = adoptionRequestRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    // Adopter sends request for a pet

    public AdoptionRequest sendRequest(
            Long petId,
            String adopterEmail) {

        User adopter = userRepository.findByEmail(adopterEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Adopter not found: " + adopterEmail));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException(
                        "Pet not found with id: " + petId));

        // Prevent duplicate requests
        if (adoptionRequestRepository.existsByAdopterIdAndPetId(
                adopter.getId(),
                pet.getId())) {

            throw new RuntimeException(
                    "You have already requested this pet");
        }

        AdoptionRequest request = new AdoptionRequest();

        request.setAdopter(adopter);
        request.setPet(pet);
        request.setStatus(RequestStatus.PENDING);

        return adoptionRequestRepository.save(request);
    }

    // Adopter sees their own requests

    public List<AdoptionRequest> getMyRequests(
            String adopterEmail) {

        User adopter = userRepository.findByEmail(adopterEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Adopter not found: " + adopterEmail));

        return adoptionRequestRepository.findByAdopterId(
                adopter.getId()
        );
    }

    public List<AdoptionRequest> getRequestsForMyPets(
            String shelterEmail) {

        return adoptionRequestRepository
                .findByPetShelterEmail(
                        shelterEmail
                );
    }

    public AdoptionRequest updateStatus(
            Long requestId,
            RequestStatus status,
            String shelterEmail) {

        AdoptionRequest request = adoptionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException(
                        "Request not found with id: " + requestId));
        String ownerEmail =
                request.getPet()
                        .getShelter()
                        .getEmail();

        if (!ownerEmail.equals(shelterEmail)) {
            throw new RuntimeException(
                    "You are not authorized to manage this request"
            );
        }

        request.setStatus(status);

        if (status == RequestStatus.APPROVED) {

            Pet pet = request.getPet();

            // Pet becomes unavailable
            pet.setStatus(PetStatus.ADOPTED);

            petRepository.save(pet);
        }

        return adoptionRequestRepository.save(request);
    }

}