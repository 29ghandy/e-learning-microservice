package org.example.enrollmentservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.config.EnrollmentPublisherConfig;
import org.example.enrollmentservice.config.EmailPublisherConfig;
import org.example.enrollmentservice.config.RabbitMQConfig;
import org.example.enrollmentservice.dtos.DiscountCacheDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishEmail(Long studentId, List<Long> courses) {
      EmailMessage message = new EmailMessage(studentId, courses);
        rabbitTemplate.convertAndSend(EmailPublisherConfig.EXCHANGE_NAME,
                EmailPublisherConfig.ROUTING_KEY,
                message);
    }

    public record EmailMessage(Long studentId, List<Long> courses) implements Serializable {}
}
