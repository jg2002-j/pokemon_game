package com.clapped.scoreboard;

import com.clapped.main.messaging.events.*;
import com.clapped.main.model.ActionType;
import com.clapped.main.model.Player;
import com.clapped.scoreboard.ws.EventsHandler;
import com.clapped.scoreboard.ws.StateSnapshot;
import com.clapped.scoreboard.ws.WsMessageWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.clapped.scoreboard.ws.WsMsgType.STATE_UPDATE;

@Slf4j
@ApplicationScoped
public class ScoreboardService {

    @Getter
    private ScoreboardState state;

    private final EventsHandler eventsHandler;
    private final ObjectMapper objectMapper;

    @Inject
    public ScoreboardService(final EventsHandler eventsHandler, final ObjectMapper objectMapper) {
        this.eventsHandler = eventsHandler;
        this.objectMapper = objectMapper;
        this.state = new ScoreboardState();
    }

    public void handleSettingsEvent(final SettingsEvent evt) {
        log.info("Received settings event from Kafka: {}", evt);
        state.setPkmnGen(evt.getGeneration());
        state.setPkmnLvl(evt.getLevel());
        eventsHandler.broadcast(serialiseState(evt.getLogMsgs()));
    }

    public void handleLobbyEvent(final LobbyEvent evt) {
        log.info("Received lobby event from Kafka: {}", evt);
        final String username = evt.getPlayer().getUsername();
        switch (evt.getJoinLeave()) {
            case JOIN -> state.getPlayers().putIfAbsent(username, evt.getPlayer());
            case LEAVE -> {
                state.getPlayers().remove(username);
                state.getPlayerTurnOptions().remove(username);
            }
        }
        eventsHandler.broadcast(serialiseState(List.of(evt.getLogMsg())));
    }

    public void handleTurnStartEvent(final TurnStartEvent evt) {
        log.info("Received turn start event from Kafka: {}", evt);
        state.setTurnNum(evt.getTurnNum());
        final Map<String, List<ActionType>> safeTurnOptions;
        if (evt.getPlayerTurnOptions() == null) {
            safeTurnOptions = new ConcurrentHashMap<>();
        } else {
            final Map<String, List<ActionType>> tmp = new HashMap<>();
            evt.getPlayerTurnOptions().forEach((k, v) -> tmp.put(k, v == null ? List.of() : List.copyOf(v)));
            safeTurnOptions = new ConcurrentHashMap<>(tmp);
        }
        state.setPlayerTurnOptions(safeTurnOptions);
        eventsHandler.broadcast(serialiseState(List.of(evt.getLogMsg())));
    }

    public void handleTurnQueueEvent(final TurnQueueEvent evt) {
        log.info("Received turn queue event from Kafka: {}", evt);
        if (evt.getTurnNum() == state.getTurnNum()) {
            final Map<String, String> safeUsernamesAndActions = evt.getUsernamesAndActions() == null
                    ? new ConcurrentHashMap<>()
                    : new ConcurrentHashMap<>(evt.getUsernamesAndActions());
            state.setUsernamesAndActions(safeUsernamesAndActions);
            eventsHandler.broadcast(serialiseState(List.of(evt.getLogMsg())));
        } else {
            log.warn("Received turn queue event with outdated turn num, ignoring.");
        }
    }

    public void handleProcessedTurnEvent(final ProcessedTurnEvent evt) {
        if (evt.getTurnNum() == state.getTurnNum()) {
            log.info("Received processed turn event from Kafka: {}", evt);
            for (final Player player : evt.getChangedPlayers()) {
                state.getPlayers().put(player.getUsername(), player);
            }
            eventsHandler.broadcast(serialiseState(evt.getLogMsgs()));
        } else {
            log.warn("Received processed turn event with outdated turn num, ignoring.");
        }
    }

    public String serialiseState(final List<String> logMsgs) {
        final WsMessageWrapper msg = new WsMessageWrapper(
                1,
                STATE_UPDATE,
                StateSnapshot.from(state),
                logMsgs
        );
        return toJson(msg);
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
