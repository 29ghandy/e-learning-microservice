package org.example.chatservice.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatservice.requestBodies.MessageRequest;
import org.example.chatservice.dtos.MessageResponse;
import org.example.chatservice.models.Message;
import org.example.chatservice.services.MessageService;
import org.example.chatservice.services.RedisService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat/{groupId}")
    public void sendMessage(
            @DestinationVariable String groupId,
            @Payload MessageRequest messageRequest) {
        messageService.sendMessage(messageRequest, groupId);
    }
}
