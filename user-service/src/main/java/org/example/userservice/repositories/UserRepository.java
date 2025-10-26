package org.example.userservice.repositories;

import org.example.userservice.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository  extends JpaRepository<Users, Long>  {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Users> findAllByEmailIn(Set<String> emails);
}
