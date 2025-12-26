package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.LobbyEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class LobbyEventConsumer {
    private final ScoreboardService service;

    @Inject
    public LobbyEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("lobby-in")
    public void consume(final LobbyEvent evt) {
        service.handleLobbyEvent(evt);
    }
}
