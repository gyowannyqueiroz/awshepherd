package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEventPublisher;
import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.util.PrintUtils;
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
            PrintUtils.printError("Profile " + name +" is not defined in the credentials file." +
                    "\nUse 'profiles' command to list the available profiles.");
            return;
        }
        CliProfileHolder.setAwsProfile(name);
        PrintUtils.printSuccess("Current AWS Profile set to " + CliProfileHolder.getAwsProfile());
        publisher.publishEvent(name);
    }

    @ShellMethod(key = "profiles", value = "Lists the profiles from the AWS credentials file")
    public void listUserProfiles() throws IOException {
        CliProfileHolder.getAwsProfiles().forEach(PrintUtils::printSuccess);
    }

    @ShellMethod(key = "env", value = "Sets environment variables")
    public void setEnvironmentVariable(String name, @ShellOption(defaultValue = "") String value) {
        CliProfileHolder.putEnv(name, value);
    }

    @ShellMethod(key = "env-ls", value = "Lists environment variables")
    public void listEnvironmentVariables() {
        CliProfileHolder.getEnvMap().entrySet()
                .stream()
                .map(entry -> entry.getKey()+" = "+entry.getValue())
                .forEach(PrintUtils::printSuccess);
    }
}
