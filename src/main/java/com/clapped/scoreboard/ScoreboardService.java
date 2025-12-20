package com.clapped.scoreboard;

import com.clapped.main.messaging.events.GameEvent;
import com.clapped.main.messaging.events.PlayerEvent;
import com.clapped.main.messaging.events.TurnActionEvent;
import com.clapped.main.messaging.events.TurnInfoEvent;
import com.clapped.main.model.Player;
import com.clapped.pokemon.model.Generation;
import com.clapped.scoreboard.ws.EventsHandler;
import com.clapped.scoreboard.ws.StateSnapshot;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ScoreboardService {

    @Getter
    private ScoreboardState state;

    private final EventsHandler eventsHandler;

    @Inject
    public ScoreboardService(final EventsHandler eventsHandler) {
        this.eventsHandler = eventsHandler;
        this.state = new ScoreboardState();
    }

    public void handleGameEvent(final GameEvent evt) {
        log.info("{} | Received {} {} event from Kafka: {}", evt.getTimestamp(), evt.getEventType(), evt.getGameEvtType(), evt);
        switch (evt.getGameEvtType()) {
            case TURN_CHANGE -> state.setTurnNum(evt.getNewVal());
            case LEVEL_CHANGE -> state.setPkmnLvl(evt.getNewVal());
            case GENERATION_CHANGE -> state.setPkmnGen(Generation.fromNum(evt.getNewVal()));
            default -> log.error("Malformed {} event: {}", evt.getEventType(), evt);
        }
        eventsHandler.broadcastState(StateSnapshot.from(state), evt.getResult());
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
        eventsHandler.broadcastState(StateSnapshot.from(state), evt.getResult());
    }

    public void handleTurnInfoEvent(final TurnInfoEvent evt) {
        log.info("{} | Received {} event from Kafka: {}", evt.getTimestamp(), evt.getEventType(), evt);
        state.getPlayerTurnOptions().clear();
        if (evt.getPlayerActionOptions() != null) {
            state.getPlayerTurnOptions().putAll(evt.getPlayerActionOptions());
        }
        eventsHandler.broadcastState(StateSnapshot.from(state), evt.getResult());
    }

    public void handleTurnActionEvent(final TurnActionEvent evt) {
        log.info("{} | Received {} {} event from Kafka: {}", evt.getTimestamp(), evt.getEventType(), evt.getTurnActionEvtTypes(), evt);
        final Player target = evt.getAffectedPlayer();
        if (!state.getPlayers().containsKey(target.getUsername())) {
            log.warn("TurnAction for unknown player: {}", target.getUsername());
            return;
        }
        state.getPlayers().put(target.getUsername(), target);
        eventsHandler.broadcastState(StateSnapshot.from(state), evt.getResult());
    }


}
