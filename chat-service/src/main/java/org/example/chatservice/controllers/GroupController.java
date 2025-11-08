package org.example.chatservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.chatservice.models.Group;
import org.example.chatservice.models.Message;
import org.example.chatservice.requestBodies.GroupRequest;
import org.example.chatservice.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getGroup(groupId));
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<Message>> getGroupMessages(
            @PathVariable String groupId,
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(groupService.getGroupMessages(groupId, limit));
    }
    @PostMapping("/create-group")
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequest requestBody) {
         try {
             return groupService.createGroup(requestBody);
         }
         catch (Exception e) {
             return ResponseEntity.badRequest().build();
         }
    }
}