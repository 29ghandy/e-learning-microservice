package org.example.chatservice.requestBodies;

import lombok.Data;

@Data
public class CreateGroupRequest {
    private String title;
    private Long teacherId;

}
