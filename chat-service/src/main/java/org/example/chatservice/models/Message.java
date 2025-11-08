package org.example.chatservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    private String id;

    private String text;
    private String userId;
    private String username;
    private String groupId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
