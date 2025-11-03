package org.example.courseservice.Config;

import org.example.courseservice.services.helper.RedisExpirationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Listen to expiration events from all databases
        container.addMessageListener(listenerAdapter,
                new org.springframework.data.redis.listener.PatternTopic("__keyevent@*__:expired"));

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisExpirationListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}
