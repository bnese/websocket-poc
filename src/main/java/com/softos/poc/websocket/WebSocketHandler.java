package com.softos.poc.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ObjectMapper objectMapper;

    @Autowired
    public WebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void broadcast(Object message) {
        TextMessageWrapper messageWrapper = new TextMessageWrapper(message);
        sessions.stream()
                .filter(WebSocketSession::isOpen)
                .forEach(messageWrapper::send);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        //echo service
        session.sendMessage(message);
    }

    class TextMessageWrapper {
        final private TextMessage message;

        TextMessageWrapper(Object message) {
            this.message = createTextMessage(message);
        }

        TextMessage createTextMessage(Object message) {
            try {
                return new TextMessage(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        void send(WebSocketSession session) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
