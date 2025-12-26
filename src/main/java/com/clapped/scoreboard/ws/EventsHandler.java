package com.clapped.scoreboard.ws;

import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
@ServerEndpoint("/scoreboard")
public class EventsHandler {

    private final ScoreboardService service;
    private final Map<String, Session> sessions;

    public EventsHandler(final ScoreboardService service) {
        this.service = service;
        this.sessions = new ConcurrentHashMap<>();
    }


    @OnOpen
    public void onOpen(final Session session) {
        log.info("Client connected: {}", session.getId());
        sessions.put(session.getId(), session);
        final String resultMsg = "Syncing game state with new connection: " + session.getId();
        session.getAsyncRemote().sendText(
                service.serialiseState(List.of(resultMsg))
        );
    }


    @OnClose
    public void onClose(final Session session) {
        log.info("Client disconnected: {}", session.getId());
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(final Session session, final Throwable error) {
        final String sessionId = session != null ? session.getId() : "<null>";
        log.error("WebSocket error (sessionId={}):", sessionId, error);
        if (session != null) {
            sessions.remove(session.getId());
        }
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
        final String trimmed = message == null ? "" : (message.length() > 100 ? message.substring(0, 100) + "..." : message);
        log.info("Sending new WS broadcast: {}", trimmed);
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }

}
