package org.example.enrollmentservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.config.PaymentPublisherConfig;
import org.example.enrollmentservice.dtos.DiscountCacheDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishPayment(Long studentId, List<Long> courses) {
        PaymentMessage message = new PaymentMessage(studentId, courses);
        rabbitTemplate.convertAndSend(PaymentPublisherConfig.EXCHANGE_NAME,
                PaymentPublisherConfig.ROUTING_KEY,
                message);
    }

    public record PaymentMessage(Long studentId, List<Long> courses) implements Serializable {}
}
