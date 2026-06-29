package com.home4paws.home4paws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class ChatPubSubConfig {

    @Bean
    public RedisMessageListenerContainer chatListener(RedisConnectionFactory cf,
            ChatSseManager mgr) {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(cf);
        container.addMessageListener((message, pattern) -> {
            String body = new String(message.getBody()); // "requestId::json"
            int i = body.indexOf("::");
            Long requestId = Long.parseLong(body.substring(0, i));
            mgr.broadcastLocal(requestId, body.substring(i + 2));
        }, new ChannelTopic("chat"));
        return container;
    }
}