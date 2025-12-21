package com.clapped.scoreboard;

import com.clapped.main.messaging.events.GameEvent;
import com.clapped.main.messaging.events.PlayerEvent;
import com.clapped.main.messaging.events.TurnActionEvent;
import com.clapped.main.messaging.events.TurnInfoEvent;
import com.clapped.main.model.Player;
import com.clapped.main.model.ProcessResult;
import com.clapped.scoreboard.ws.EventsHandler;
import com.clapped.scoreboard.ws.StateSnapshot;
import com.clapped.scoreboard.ws.WsMessageWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.clapped.scoreboard.ws.WsMsgType.ERROR;
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

    public void handleGameEvent(final GameEvent evt) {
        log.info("{} | Received {} {} event from Kafka: {}", evt.getTimestamp(), evt.getEventType(), evt.getGameEvtType(), evt);
        switch (evt.getGameEvtType()) {
            case TURN_CHANGE -> state.setTurnNum(evt.getNewVal());
            case LEVEL_CHANGE -> state.setPkmnLvl(evt.getNewVal());
            case GENERATION_CHANGE -> state.setPkmnGen(evt.getNewVal());
            default -> log.error("Malformed {} event: {}", evt.getEventType(), evt);
        }
        eventsHandler.broadcast(serialiseState(evt.getResult()));
    }

    public void handlePlayerEvent(final PlayerEvent evt) {
        log.info("{} | Received {} {} event from Kafka: {}", evt.getTimestamp(), evt.getEventType(), evt.getPlayerEvtType(), evt);
        final String username = evt.getPlayer().getUsername();
        switch (evt.getPlayerEvtType()) {
            case JOIN -> state.getPlayers().putIfAbsent(username, evt.getPlayer());
            case LEAVE -> {
                state.getPlayers().remove(username);
                state.getPlayerTurnOptions().remove(username);
            }
            default -> log.error("Malformed {} event: {}", evt.getEventType(), evt);
        }
        eventsHandler.broadcast(serialiseState(evt.getResult()));
    }

    public void handleTurnInfoEvent(final TurnInfoEvent evt) {
        log.info("{} | Received {} event from Kafka: {}", evt.getTimestamp(), evt.getEventType(), evt);
        state.getPlayerTurnOptions().clear();
        if (evt.getPlayerActionOptions() != null) {
            state.getPlayerTurnOptions().putAll(evt.getPlayerActionOptions());
        }
        eventsHandler.broadcast(serialiseState(evt.getResult()));
    }

    public void handleTurnActionEvent(final TurnActionEvent evt) {
        log.info("{} | Received {} {} event from Kafka: {}", evt.getTimestamp(), evt.getEventType(), evt.getTurnActionEvtTypes(), evt);
        final Player target = evt.getAffectedPlayer();
        if (!state.getPlayers().containsKey(target.getUsername())) {
            log.warn("TurnAction for unknown player: {}", target.getUsername());
            return;
        }
        state.getPlayers().put(target.getUsername(), target);
        eventsHandler.broadcast(serialiseState(evt.getResult()));
    }

    public String serialiseState(final ProcessResult result) {
        final WsMessageWrapper msg = new WsMessageWrapper(
                1,
                result.isSuccess() ? STATE_UPDATE : ERROR,
                StateSnapshot.from(state),
                result
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
