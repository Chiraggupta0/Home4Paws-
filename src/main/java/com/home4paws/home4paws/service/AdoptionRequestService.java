package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.*;
import com.home4paws.home4paws.repository.AdoptionRequestRepository;
import com.home4paws.home4paws.repository.PetRepository;
import com.home4paws.home4paws.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.home4paws.home4paws.model.PetStatus;

import java.util.List;

@Service
@Slf4j
public class AdoptionRequestService {

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final SubscriptionService subscriptionService;

    public AdoptionRequestService(
            AdoptionRequestRepository adoptionRequestRepository,
            UserRepository userRepository,
            PetRepository petRepository,
            SubscriptionService subscriptionService) {

        this.adoptionRequestRepository = adoptionRequestRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.subscriptionService = subscriptionService;
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

        // Seller (priced) listings require the buyer to be subscribed before requesting.
        // NGO (free) listings stay open to everyone.
        if (pet.getPrice() != null && !subscriptionService.isSubscribed(adopterEmail)) {
            throw new RuntimeException("Subscribe to send a request to this seller");
        }

        // Prevent duplicate requests
        if (adoptionRequestRepository.existsByAdopterIdAndPetId(
                adopter.getId(),
                pet.getId())) {

            log.warn("Duplicate adoption request by {} for pet id={}", adopterEmail, petId);
            throw new RuntimeException(
                    "You have already requested this pet");
        }

        AdoptionRequest request = new AdoptionRequest();

        request.setAdopter(adopter);
        request.setPet(pet);
        request.setStatus(RequestStatus.PENDING);

        AdoptionRequest saved = adoptionRequestRepository.save(request);
        log.info("Adoption request created id={} adopter={} pet id={}", saved.getId(), adopterEmail, petId);
        return saved;
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
            log.warn("Unauthorized status change on request id={} by {}", requestId, shelterEmail);
            throw new RuntimeException(
                    "You are not authorized to manage this request"
            );
        }

        // Seller (priced) listings require the seller to be subscribed to accept/reject buyer requests.
        // NGO (free) listings stay open.
        if (request.getPet().getPrice() != null && !subscriptionService.isSubscribed(shelterEmail)) {
            throw new RuntimeException("Subscribe to manage requests on your listings");
        }

        request.setStatus(status);

        if (status == RequestStatus.APPROVED) {

            Pet pet = request.getPet();

            // Pet becomes unavailable
            pet.setStatus(PetStatus.ADOPTED);

            petRepository.save(pet);
        }

        AdoptionRequest saved = adoptionRequestRepository.save(request);
        log.info("Adoption request id={} set to {} by shelter={}", requestId, status, shelterEmail);
        return saved;
    }

}