package org.example.chatservice.requestBodies;

import lombok.Data;

@Data
public class JoinGroupRequest {
    private String groupId;
    private Long newMemberId;
    private String username;
}
