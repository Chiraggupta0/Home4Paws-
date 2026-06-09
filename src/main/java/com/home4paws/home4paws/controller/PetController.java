package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.model.Pet;
import com.home4paws.home4paws.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Anyone logged in can view all available pets
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllAvailablePets());
    }

    // Anyone logged in can view a pet by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    // Only SHELTER users can add pets
    @PostMapping
    @PreAuthorize("hasRole('NGO_SHELTER')")
    public ResponseEntity<Pet> addPet(
            @RequestBody Pet pet,
            Principal principal) {

        return ResponseEntity.ok(
                petService.addPet(
                        pet,
                        principal.getName()
                )
        );
    }

    // Only SHELTER users can delete pets
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('NGO_SHELTER')")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long id,
            Principal principal) {

        petService.deletePet(
                id,
                principal.getName()
        );

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/my-pets")
    @PreAuthorize("hasRole('NGO_SHELTER')")
    public ResponseEntity<List<Pet>> getMyPets(
            Principal principal) {

        return ResponseEntity.ok(
                petService.getMyPets(
                        principal.getName()
                )
        );
    }
}