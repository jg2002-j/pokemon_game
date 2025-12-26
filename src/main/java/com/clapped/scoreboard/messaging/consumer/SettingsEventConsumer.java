package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.SettingsEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class SettingsEventConsumer {
    private final ScoreboardService service;

    @Inject
    public SettingsEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("settings-in")
    public void consume(final SettingsEvent evt) {
        service.handleSettingsEvent(evt);
    }
}
