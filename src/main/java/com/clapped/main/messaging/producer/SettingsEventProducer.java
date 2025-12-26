package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.SettingsEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class SettingsEventProducer {
    @Inject
    @Channel("settings")
    Emitter<SettingsEvent> emitter;

    public void sendSettingsEvent(final SettingsEvent evt) {
        log.info("Sending new settings event: {}", evt);
        emitter.send(evt);
    }
}
