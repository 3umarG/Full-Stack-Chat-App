package com.chat.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class SSEController {

    /**
     * CopyOnWriteArrayList: This thread-safe list is used to store active SseEmitter instances.
     * Each client connection is represented by an SseEmitter.
     */
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

    public SSEController() {
        startSendingMessages();
    }

    /**
     * SseEmitter: This class is used to handle Server-Sent Events.
     * It sends events to the client until it is completed or timed out.
     */
    @GetMapping("/sse")
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter(); // set the timeout in milliseconds
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            log.error("SSE Emitter Timeout ..!!!");
            emitters.remove(emitter);
        });
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    private void startSendingMessages() {
        scheduledExecutor.scheduleAtFixedRate(() -> {
            String message = "{\"time\":\"" + LocalTime.now().toString() + "\"}";
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .data(message)
                            .name("json-message"));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            });
        }, 0, 2, TimeUnit.SECONDS);
    }
}
