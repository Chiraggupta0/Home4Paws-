package com.home4paws.home4paws.repository;

import com.home4paws.home4paws.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // find a user by their email
    Optional<User> findByEmail(String email);

    // check if email already exists (for registration)
    boolean existsByEmail(String email);
}