package org.example.chatservice.requestBodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class GroupRequest {
    private String title;
    private Long teacherId;

}
