package org.example.enrollmentservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnrollmentPublisherConfig {
    public static final String EXCHANGE_NAME = "enrollment.exchange";
    public static final String ROUTING_KEY = "enrollment.discount.decremented";
    public static final String QUEUE_NAME = "course.discount.update.queue";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public TopicExchange enrollmentExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue courseUpdateQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding enrollmentToCourseBinding(Queue courseUpdateQueue, TopicExchange enrollmentExchange) {
        return BindingBuilder.bind(courseUpdateQueue).to(enrollmentExchange).with(ROUTING_KEY);
    }

}
