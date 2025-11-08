package org.example.chatservice.services;

import lombok.RequiredArgsConstructor;
import org.example.chatservice.models.Group;
import org.example.chatservice.models.Message;
import org.example.chatservice.repositories.GroupRepository;
import org.example.chatservice.repositories.MessageRepository;
import org.example.chatservice.requestBodies.GroupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final RedisService redisService;

    public Group getGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    public List<Message> getGroupMessages(String groupId, int limit) {
        // Get messages from database
        List<Message> dbMessages = messageRepository.findByGroupIdOrderByCreatedAtDesc(groupId);

        // Get cached messages
        List<Message> cachedMessages = redisService.getCachedMessages(groupId);

        // Combine and return latest messages
        return Stream.concat(cachedMessages.stream(), dbMessages.stream())
                .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public ResponseEntity<Group> createGroup(GroupRequest request) {
        Group group = new Group();
        group.setTitle(request.getTitle());
        group.setTeacherId(request.getTeacherId());
        group.setCreatedAt(LocalDateTime.now());
        groupRepository.save(group);
        return ResponseEntity.ok(group);
    }
}
