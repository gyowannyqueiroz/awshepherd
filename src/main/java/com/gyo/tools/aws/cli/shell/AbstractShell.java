package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEvent;
import org.springframework.context.ApplicationListener;

public abstract class AbstractShell implements ApplicationListener<ProfileChangedEvent> {

    @Override
    public void onApplicationEvent(ProfileChangedEvent event) {
        resetServices();
    }

    abstract void resetServices();
}
