package org.example.chatservice.repositories;

import org.example.chatservice.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// message
//member
// to
public interface ChatRepository  extends MongoRepository<Group,String> {
    Optional<Group> findByName(String name);

}
