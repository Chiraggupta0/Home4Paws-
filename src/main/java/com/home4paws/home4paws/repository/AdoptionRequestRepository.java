package com.home4paws.home4paws.repository;

import com.home4paws.home4paws.model.AdoptionRequest;
import com.home4paws.home4paws.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {

    // find all requests made by a specific adopter
    List<AdoptionRequest> findByAdopterId(Long adopterId);

    // find all requests for a specific pet
    List<AdoptionRequest> findByPetId(Long petId);

    // find all requests with a specific status
    List<AdoptionRequest> findByStatus(RequestStatus status);

    // find all requests by a specific adopter with a specific status
    List<AdoptionRequest> findByAdopterIdAndStatus(Long adopterId, RequestStatus status);

    List<AdoptionRequest> findByPetShelterEmail(String email);

    boolean existsByAdopterIdAndPetId(
            Long adopterId,
            Long petId
    );
}