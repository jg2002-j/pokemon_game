package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.LobbyEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class LobbyEventProducer {
    @Inject
    @Channel("lobby")
    Emitter<LobbyEvent> emitter;

    public void sendLobbyEvent(final LobbyEvent evt) {
        log.info("Sending new lobby event: {}", evt);
        emitter.send(evt);
    }
}
