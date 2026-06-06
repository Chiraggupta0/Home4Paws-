package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.model.Pet;
import com.home4paws.home4paws.service.PetService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllAvailablePets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet, Principal principal) {
        return ResponseEntity.ok(petService.addPet(pet, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id, Principal principal) {
        petService.deletePet(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}