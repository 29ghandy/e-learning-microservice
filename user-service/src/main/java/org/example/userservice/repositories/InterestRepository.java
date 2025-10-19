package org.example.userservice.repositories;

import org.example.userservice.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InterestRepository extends JpaRepository<Interest, Long> {
}
