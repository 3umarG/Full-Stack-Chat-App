package com.chat.api.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.chat.api.utils.Constants.PUBLIC_MESSAGE_BROKER_FULL_DESTINATION;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageContent = new String(message.getBody());
        template.convertAndSend(PUBLIC_MESSAGE_BROKER_FULL_DESTINATION,messageContent);
    }
}
