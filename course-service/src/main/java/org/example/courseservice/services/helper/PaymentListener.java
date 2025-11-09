package org.example.courseservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.dtos.DiscountCacheDTO;
import org.example.courseservice.repositories.CourseRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentListener {

    private final RedisService redisService;
    @RabbitListener(queues =  "course.payment.update.queue")
    public void handlePayment(PaymentMessage message) {

        redisService.savePayment(message.studentId, message.courses);
    }

    public record PaymentMessage(Long studentId, List<Long> courses) implements Serializable {}
}