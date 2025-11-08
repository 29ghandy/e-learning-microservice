package org.example.chatservice.repositories;

import org.example.chatservice.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {
    List<Message> findByGroupIdOrderByCreatedAtDesc(String groupId);
}
