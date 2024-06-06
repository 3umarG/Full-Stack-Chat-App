package com.chat.api.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // to connect on the websocket server host : localhost:8080/chat
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /app will be the default path for all external endpoints for sending messages
        registry.setApplicationDestinationPrefixes("/app");

        // there are two brokers here,
        // first with path /chatroom for all public messages "topic"
        // and second with path /user for only private messages for certain users
        // there will be N queues, where N is the number of users
        // for each user there will be a queue to accept messages : /user/{USER_ID}/private
        registry.enableSimpleBroker("/chatroom", "/user");

        /***
         * Configure the prefix used to identify user destinations.
         * User destinations provide the ability for a user to subscribe to queue names unique to their session as well as
         * for others to send messages to those unique, user-specific queues.
         * For example when a user attempts to subscribe to "/user/queue/position-updates",
         * the destination may be translated to "/queue/position-updates-useri9oqdfzo"
         * yielding a unique queue name that does not collide with any other user attempting to do the same.
         * Subsequently, when messages are sent to "/user/{username}/queue/position-updates",
         * the destination is translated to "/queue/position-updates-useri9oqdfzo".
         * The default prefix used to identify such destinations is "/user/"
         */
        registry.setUserDestinationPrefix("/user");
    }
}
