package org.example.userservice.repositories;

import org.example.userservice.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
}
