package org.example.chatservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("groups")
@RequiredArgsConstructor
@Data
public class Group {

    @Id
    private String id;
    private String title;
    private long numberOfMembers;
    private long teacherId;
    private List<Member> members;
}
