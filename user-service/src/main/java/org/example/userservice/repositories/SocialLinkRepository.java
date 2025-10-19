package org.example.userservice.repositories;

import org.example.userservice.models.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
}
