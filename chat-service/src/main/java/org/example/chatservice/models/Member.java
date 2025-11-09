package org.example.chatservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    private String id;

    private Long userId;
    private String username;
    private String type;
    private LocalDateTime joinedAt;

    private String groupId;
}
