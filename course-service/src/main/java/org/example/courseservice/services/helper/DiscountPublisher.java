package org.example.courseservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.Config.RabbitMQConfig;
import org.example.courseservice.dtos.DiscountCacheDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishDiscount(Long courseId, DiscountCacheDTO discount) {
        DiscountMessage message = new DiscountMessage(courseId, discount);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message);
    }

    public record DiscountMessage(Long courseId, DiscountCacheDTO discount) {}
}
