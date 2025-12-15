package com.clapped.scoreboard.ws;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
@ServerEndpoint("/scoreboard")
public class EventsHandler {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(final Session session) {
        log.info("Client connected: {}", session.getId());
        sessions.put(session.getId(), session);

        // TODO: Send current state

    }


    @OnClose
    public void onClose(final Session session) {
        log.info("Client disconnected: {}", session.getId());
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(final Session session, final Throwable error) {
        log.error("Error: {}", error.getMessage());
        sessions.remove(session.getId());
    }

    @OnMessage
    public void onMessage(final Session session, final String message) {
        if ("ping".equalsIgnoreCase(message)) {
            session.getAsyncRemote().sendText("pong");
            return;
        }
        log.info("Received: {}", message);
    }

    public void broadcast(final String message) {
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }

}

