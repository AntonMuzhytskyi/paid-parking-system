package com.parking.samurai.repository;

import com.parking.samurai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
* Repository interface for managing User entities.
* Extends JpaRepository to provide standard CRUD operations.
* Includes a query method used by Spring Security to load users during authentication.
*/

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}