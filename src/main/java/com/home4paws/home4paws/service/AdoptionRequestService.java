package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.AdoptionRequest;
import com.home4paws.home4paws.model.Pet;
import com.home4paws.home4paws.model.RequestStatus;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.AdoptionRequestRepository;
import com.home4paws.home4paws.repository.PetRepository;
import com.home4paws.home4paws.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}