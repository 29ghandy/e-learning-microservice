package org.example.enrollmentservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.dtos.DiscountCacheDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountListener {
      private final   RedisService redisService;
    @RabbitListener(queues = "enrollment.discount.queue")
    public void handleDiscountMessage(DiscountMessage message) {
        System.out.println("Received discount message for course {}: {} "+
                message.courseId() + message.discount());
        Duration ttl = Duration.between(LocalDateTime.now(), message.discount.getDiscountEndDate());
        redisService.saveDiscount(message.courseId,message.discount,ttl);
    }

    public record DiscountMessage(Long courseId, DiscountCacheDTO discount) {}
}
