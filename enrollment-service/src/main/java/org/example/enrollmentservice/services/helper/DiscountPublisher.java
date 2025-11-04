package org.example.enrollmentservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.config.EnrollmentPublisherConfig;
import org.example.enrollmentservice.config.RabbitMQConfig;
import org.example.enrollmentservice.dtos.DiscountCacheDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class DiscountPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishDiscount(Long courseId, DiscountCacheDTO discount) {
        DiscountMessage message = new DiscountMessage(courseId, discount);
        rabbitTemplate.convertAndSend(EnrollmentPublisherConfig.EXCHANGE_NAME,
                EnrollmentPublisherConfig.ROUTING_KEY,
                message);
    }

    public record DiscountMessage(Long courseId, DiscountCacheDTO discount) implements Serializable {}
}
