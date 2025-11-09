package org.example.chatservice.repositories;

import org.example.chatservice.models.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member,Long> {
    Member findByUserIdAndGroupId(Long userId, String groupId);
}
