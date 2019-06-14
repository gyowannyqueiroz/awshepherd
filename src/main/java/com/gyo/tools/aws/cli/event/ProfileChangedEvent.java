package com.gyo.tools.aws.cli.event;

import org.springframework.context.ApplicationEvent;

public class ProfileChangedEvent extends ApplicationEvent {
    private String profile;

    public ProfileChangedEvent(Object source, String profile) {
        super(source);
        this.profile = profile;
    }
    public String getProfile() {
        return profile;
    }
}
