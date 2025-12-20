package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.PlayerEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PlayerEventConsumer {

    private final ScoreboardService service;

    @Inject
    public PlayerEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("player-in")
    public void consume(final PlayerEvent evt) {
        service.handlePlayerEvent(evt);
    }

}
