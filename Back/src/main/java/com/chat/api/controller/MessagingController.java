package com.chat.api.controller;

import com.chat.api.DTOs.MessageDto;
import com.chat.api.redis.RedisPublisher;
import com.chat.api.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessagingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisPublisher publisher;

    @MessageMapping("/public-message") // /app/public-message
    public MessageDto sendPublicMessage(@Payload MessageDto message) {
        publisher.publish(Constants.REDIS_CHANNEL_NAME, message);
        return message;
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload MessageDto message) {

        // user/{USER_NAME}/messages
        messagingTemplate.convertAndSendToUser(message.receiverName(), "/messages", message);
        System.out.println(message);
    }
}
