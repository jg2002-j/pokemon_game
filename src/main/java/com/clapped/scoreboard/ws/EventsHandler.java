package com.clapped.scoreboard.ws;

import com.clapped.main.model.ProcessResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.clapped.scoreboard.ws.WsMsgType.ERROR;
import static com.clapped.scoreboard.ws.WsMsgType.STATE_UPDATE;

@Slf4j
@ApplicationScoped
@ServerEndpoint("/scoreboard")
public class EventsHandler {

    @Inject
    ObjectMapper objectMapper;

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(final Session session) {
        log.info("Client connected: {}", session.getId());
        sessions.put(session.getId(), session);
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
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }

    public void broadcastState(final StateSnapshot state, final ProcessResult result) {
        final WsMessageWrapper msg = new WsMessageWrapper(
                1,
                result.isSuccess() ? STATE_UPDATE : ERROR,
                state,
                result
        );
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendText(toJson(msg));
        });
    }

    private String toJson(final WsMessageWrapper msg) {
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (Exception e) {
            log.error("Failed to serialize: ", e);
            return "SERVER-SIDE ERROR";
        }
    }

}
