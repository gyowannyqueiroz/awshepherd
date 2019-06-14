package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEventPublisher;
import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.util.ShellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CliShell {

    @Autowired
    private ProfileChangedEventPublisher publisher;

    @ShellMethod(key = "profile", value = "Sets the AWS credentials profile")
    public void setAwsProfile(@ShellOption String name) {
        CliProfileHolder.setAwsProfile(name);
        ShellUtils.printSuccess("Current AWS Profile set to " + CliProfileHolder.getAwsProfile());
        publisher.publishEvent(name);
    }
}
