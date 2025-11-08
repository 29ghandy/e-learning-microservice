package org.example.chatservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatservice.models.Message;
import org.example.chatservice.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    @Value("${chat.message-cache-limit:100}")
    private int cacheLimit;

    private final RedisTemplate<String, Object> redisTemplate;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    private static final String CACHE_KEY_PREFIX = "group:messages:";

    public void addMessage(String groupId, Message message) {
        String cacheKey = CACHE_KEY_PREFIX + groupId;

        try {
            // Add message to Redis list
            redisTemplate.opsForList().rightPush(cacheKey, message);

            // Set expiration (24 hours)
            redisTemplate.expire(cacheKey, 24, TimeUnit.HOURS);

            Long size = redisTemplate.opsForList().size(cacheKey);
            log.info("Message added to cache for group {}. Cache size: {}", groupId, size);

            // Check if we need to flush
            if (size != null && size >= cacheLimit) {
                flushToDatabase(groupId);
            }
        } catch (Exception e) {
            log.error("Error adding message to cache", e);
        }
    }

    @Async
    public void flushToDatabase(String groupId) {
        String cacheKey = CACHE_KEY_PREFIX + groupId;

        try {
            // Get all messages from cache
            List<Object> cachedObjects = redisTemplate.opsForList().range(cacheKey, 0, -1);

            if (cachedObjects == null || cachedObjects.isEmpty()) {
                return;
            }

            List<Message> messages = new ArrayList<>();
            for (Object obj : cachedObjects) {
                if (obj instanceof Message) {
                    messages.add((Message) obj);
                }
            }

            if (!messages.isEmpty()) {
                log.info("Flushing {} messages to database for group {}", messages.size(), groupId);
                messageRepository.saveAll(messages);

                // Clear the cache after successful save
                redisTemplate.delete(cacheKey);
                log.info("Successfully flushed and cleared cache for group {}", groupId);
            }
        } catch (Exception e) {
            log.error("Error flushing messages to database for group: {}", groupId, e);
        }
    }

    public List<Message> getCachedMessages(String groupId) {
        String cacheKey = CACHE_KEY_PREFIX + groupId;
        List<Message> messages = new ArrayList<>();

        try {
            List<Object> cachedObjects = redisTemplate.opsForList().range(cacheKey, 0, -1);

            if (cachedObjects != null) {
                for (Object obj : cachedObjects) {
                    if (obj instanceof Message) {
                        messages.add((Message) obj);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting cached messages", e);
        }

        return messages;
    }

    public void flushAllCaches() {
        log.info("Flushing all caches to database");

        try {
            Set<String> keys = redisTemplate.keys(CACHE_KEY_PREFIX + "*");

            if (keys != null) {
                for (String key : keys) {
                    String groupId = key.replace(CACHE_KEY_PREFIX, "");
                    flushToDatabase(groupId);
                }
            }
        } catch (Exception e) {
            log.error("Error flushing all caches", e);
        }
    }
}