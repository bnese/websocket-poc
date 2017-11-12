package com.softos.poc.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CountingService {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final WebSocketHandler wsHandler;

    @Autowired
    public CountingService(WebSocketHandler wsHandler) {
        this.wsHandler = wsHandler;
    }

    @Scheduled(fixedDelay = 1500)
    public void incrementAndBroadcast() {
        wsHandler.broadcast(counter.incrementAndGet());
    }
}