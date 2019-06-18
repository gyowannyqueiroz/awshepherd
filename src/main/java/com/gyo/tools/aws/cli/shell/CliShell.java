package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEventPublisher;
import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.util.ShellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

@ShellComponent
public class CliShell {

    @Autowired
    private ProfileChangedEventPublisher publisher;

    @ShellMethod(key = "profile", value = "Sets the AWS credentials profile")
    public void setAwsProfile(@ShellOption String name) {
        if (!CliProfileHolder.getAwsProfiles().contains(name)) {
            ShellUtils.printError("Profile " + name +" is not defined in the credentials file." +
                    "\nUse 'profiles' command to list the available profiles.");
            return;
        }
        CliProfileHolder.setAwsProfile(name);
        ShellUtils.printSuccess("Current AWS Profile set to " + CliProfileHolder.getAwsProfile());
        publisher.publishEvent(name);
    }

    @ShellMethod(key = "profiles", value = "List the profiles from the AWS credentials file")
    public void listUserProfiles() throws IOException {
        CliProfileHolder.getAwsProfiles().forEach(ShellUtils::printSuccess);
    }
}
