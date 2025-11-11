package org.example.courseservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.Config.CoursePublisherConfig;
import org.example.courseservice.Config.RabbitMQConfig;
import org.example.courseservice.dtos.CourseCreatedDTO;
import org.example.courseservice.dtos.DiscountCacheDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class CoursePublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishCourse(CourseCreatedDTO course) {
        CourseMessage message = new CourseMessage(course);
        rabbitTemplate.convertAndSend(CoursePublisherConfig.EXCHANGE_NAME,
                CoursePublisherConfig.ROUTING_KEY,
                message);
    }

    public record CourseMessage(CourseCreatedDTO course) implements Serializable {}
}
