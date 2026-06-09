package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.model.AdoptionRequest;
import com.home4paws.home4paws.model.RequestStatus;
import com.home4paws.home4paws.service.AdoptionRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class AdoptionRequestController {

    private final AdoptionRequestService adoptionRequestService;

    public AdoptionRequestController(
            AdoptionRequestService adoptionRequestService) {
        this.adoptionRequestService = adoptionRequestService;
    }

    // Adopter sends adoption request

    @PostMapping("/{petId}")
    @PreAuthorize("hasRole('NORMAL_USER')")
    public ResponseEntity<AdoptionRequest> sendRequest(
            @PathVariable Long petId,
            Principal principal) {

        return ResponseEntity.ok(
                adoptionRequestService.sendRequest(
                        petId,
                        principal.getName()
                )
        );
    }

    // Adopter views own requests

    @GetMapping("/my")
    @PreAuthorize("hasRole('NORMAL_USER')")
    public ResponseEntity<List<AdoptionRequest>> getMyRequests(
            Principal principal) {

        return ResponseEntity.ok(
                adoptionRequestService.getMyRequests(
                        principal.getName()
                )
        );
    }

    @GetMapping("/my-pets")
    @PreAuthorize("hasRole('NGO_SHELTER')")
    public ResponseEntity<List<AdoptionRequest>> getRequestsForMyPets(
            Principal principal) {

        return ResponseEntity.ok(
                adoptionRequestService.getRequestsForMyPets(
                        principal.getName()
                )
        );
    }

    // Shelter approves request

    @PutMapping("/{requestId}/approve")
    @PreAuthorize("hasRole('NGO_SHELTER')")
    public ResponseEntity<AdoptionRequest> approveRequest(
            @PathVariable Long requestId,
            Principal principal) {

        return ResponseEntity.ok(
                adoptionRequestService.updateStatus(
                        requestId,
                        RequestStatus.APPROVED,
                        principal.getName()
                )
        );
    }

    // Shelter rejects request

    @PutMapping("/{requestId}/reject")
    @PreAuthorize("hasRole('NGO_SHELTER')")
    public ResponseEntity<AdoptionRequest> rejectRequest(
            @PathVariable Long requestId,
            Principal principal) {

        return ResponseEntity.ok(
                adoptionRequestService.updateStatus(
                        requestId,
                        RequestStatus.REJECTED,
                        principal.getName()
                )
        );
    }
}