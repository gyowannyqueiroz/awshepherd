package com.gyo.tools.aws.cli.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProfileChangedEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final String profile) {
        ProfileChangedEvent event = new ProfileChangedEvent(this, profile);
        applicationEventPublisher.publishEvent(event);
    }
}
