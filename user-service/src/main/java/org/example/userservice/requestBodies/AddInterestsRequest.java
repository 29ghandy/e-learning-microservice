package org.example.userservice.requestBodies;

import lombok.Data;

import java.util.List;

@Data
public class AddInterestsRequest {
   private List<String> interests;
   private long studentId;
}
