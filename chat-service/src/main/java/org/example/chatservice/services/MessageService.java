package org.example.chatservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatservice.dtos.MessageResponse;
import org.example.chatservice.models.Message;
import org.example.chatservice.repositories.MessageRepository;
import org.example.chatservice.requestBodies.MessageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

     private final MessageRepository messageRepository;
     private final RedisService redisService;
    private final SimpMessagingTemplate messagingTemplate;

    void sendMessage(MessageRequest request,String groupId) {
         log.info("Received message for group {}: {}", groupId, request.getText());

         // Create message entity
         Message message = Message.builder()
                 .id(UUID.randomUUID().toString())
                 .text(request.getText())
                 .userId(request.getUserId())
                 .username(request.getUsername())
                 .groupId(groupId)
                 .createdAt(LocalDateTime.now())
                 .updatedAt(LocalDateTime.now())
                 .build();

         // Add to cache
         redisService.addMessage(groupId, message);

         // Broadcast to all subscribers of this group
         MessageResponse response = MessageResponse.builder()
                 .id(message.getId())
                 .text(message.getText())
                 .userId(message.getUserId())
                 .username(message.getUsername())
                 .groupId(message.getGroupId())
                 .createdAt(message.getCreatedAt())
                 .build();

         messagingTemplate.convertAndSend("/topic/group/" + groupId, response);
     }
}
