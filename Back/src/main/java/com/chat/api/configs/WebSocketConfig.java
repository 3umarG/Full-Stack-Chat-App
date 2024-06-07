package com.chat.api.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.chat.api.utils.Constants.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // to connect on the websocket server host : localhost:8080/chat
        registry.addEndpoint(WEB_SOCKET_CONNECTION_PATH)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /app will be the default path for all external endpoints for sending messages
        registry.setApplicationDestinationPrefixes(APP_DESTINATION_PREFIX);

        // there are two brokers here,
        // first with path /chatroom for all public messages "topic"
        // and second with path /user for only private messages for certain users
        // there will be N queues, where N is the number of users
        // for each user there will be a queue to accept messages : /user/{USER_ID}/private
        registry.enableSimpleBroker(PUBLIC_MESSAGE_BROKER_PREFIX, PRIVATE_MESSAGE_BROKER_PREFIX);

        registry.setUserDestinationPrefix(USER_DESTINATION_PREFIX);
    }
}
