package org.example.userservice.repositories;

import org.example.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    void deleteByUsername(String username);
    void updatePassword(String username, String password);
}
