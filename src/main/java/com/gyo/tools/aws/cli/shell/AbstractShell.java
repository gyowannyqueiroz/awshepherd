package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEvent;
import com.gyo.tools.aws.cli.model.CliEnvironment;
import org.springframework.context.ApplicationListener;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

public abstract class AbstractShell implements ApplicationListener<ProfileChangedEvent> {

    private CliEnvironment cliEnvironment;

    public AbstractShell(CliEnvironment cliEnvironment) {
        this.cliEnvironment = cliEnvironment;
    }

    @Override
    public void onApplicationEvent(ProfileChangedEvent event) {
        resetServices();
    }

    public String formatStringWithEnvVariables(final String value) {
        Properties envProps = new Properties();
        envProps.putAll(cliEnvironment.getEnvMap());
        return new PropertyPlaceholderHelper("${", "}").replacePlaceholders(value, envProps);
    }

    public CliEnvironment getCliEnvironment() {
        return cliEnvironment;
    }

    abstract void resetServices();
}
