package com.chat.api.controller;

import com.chat.api.DTOs.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessagingController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/public-message") // /app/public-message
    @SendTo("/chatroom/messages") // this will be the destination of the public topic
    public MessageDto sendPublicMessage(@Payload MessageDto message) {
        return message;
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload MessageDto message) {

        // user/{USER_NAME}/messages
        messagingTemplate.convertAndSendToUser(message.receiverName(), "/messages", message);
        System.out.println(message);
    }
}
