package org.example.userservice.repositories;

import org.example.userservice.models.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    Optional<SocialLink> findByUserId(Long userId);
}
