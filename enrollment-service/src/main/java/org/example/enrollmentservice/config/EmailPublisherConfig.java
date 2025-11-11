package org.example.enrollmentservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailPublisherConfig {
    public static final String EXCHANGE_NAME = "email.exchange";
    public static final String ROUTING_KEY = "email.announcement.send";
    public static final String QUEUE_NAME = "email.announcement.queue";

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
    public TopicExchange emailExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding enrollmentToCourseBinding(Queue emailQueue, TopicExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(ROUTING_KEY);
    }

}
