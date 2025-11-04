package org.example.courseservice.services.helper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courseservice.dtos.DiscountCacheDTO;
import org.example.courseservice.repositories.CourseRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountListener {

    private final RedisService redisService;
    private final CourseRepository courseRepository;

    @RabbitListener(queues = "course.discount.update.queue")
    public void handleDiscount(DiscountMessage message) {
        // update cache
        // if members == 0, update database
        redisService.deleteDiscount(message.courseId);
        if (message.discount.getDiscountNumberOfMembers() == 0) {
            courseRepository.findById(message.courseId).ifPresent(course -> {
                course.setDiscountPercentage(0.0);
                course.setDiscountNumberOfMembers(0L);
                course.setDiscountStartDate(null);
                course.setDiscountEndDate(null);
                courseRepository.save(course);
            });
            return;
        }

        Duration ttl = Duration.between(LocalDateTime.now(), message.discount.getDiscountEndDate());
        redisService.saveDiscount(message.courseId, message.discount, ttl);
    }

    public record DiscountMessage(Long courseId, DiscountCacheDTO discount) {}
}
