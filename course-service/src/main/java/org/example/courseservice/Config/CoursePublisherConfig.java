package org.example.courseservice.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoursePublisherConfig {

    public static final String EXCHANGE_NAME = "course.announcement.exchange";
    public static final String ROUTING_KEY = "course.created.announcement";
    public static final String QUEUE_NAME = "course.created.update.queue";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange announcementExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue courseCreatedUpdateQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding courseToNotificationBinding(Queue courseCreatedUpdateQueue, TopicExchange announcementExchange) {
        return BindingBuilder.bind(courseCreatedUpdateQueue).to(announcementExchange).with(ROUTING_KEY);
    }

}
