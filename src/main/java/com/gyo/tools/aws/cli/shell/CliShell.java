package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEventPublisher;
import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

@ShellComponent
public class CliShell {
    private ProfileChangedEventPublisher publisher;
    private CliEnvironment cliEnvironment;
    
    @Autowired
    public CliShell(ProfileChangedEventPublisher publisher, CliEnvironment cliEnvironment) {
        this.publisher = publisher;
        this.cliEnvironment = cliEnvironment;
    }

    @ShellMethod(key = "profile", value = "Sets the AWS credentials profile")
    public void setAwsProfile(@ShellOption String name) {
        if (!cliEnvironment.profileExists(name)) {
            PrintUtils.printError("Profile " + name +" is not defined in the credentials file." +
                    "\nUse 'profiles' command to list the available profiles.");
            return;
        }
        cliEnvironment.setAwsProfile(name);
        PrintUtils.printSuccess("Current AWS Profile set to " + cliEnvironment.getAwsProfile());
        publisher.publishEvent(name);
    }

    @ShellMethod(key = "profile-ls", value = "Lists the profiles from the AWS credentials file")
    public void listAwsProfiles() throws IOException {
        cliEnvironment.getAwsProfiles().forEach(PrintUtils::printSuccess);
    }

    @ShellMethod(key = "profile-refresh", value = "Reloads the profiles from the AWS credentials file")
    public void refreshAwsProfiles() {
        cliEnvironment.loadAwsProfiles();
    }

    @ShellMethod(key = "env", value = "Sets environment variables")
    public void setEnvironmentVariable(String name, @ShellOption(defaultValue = "") String value) {
        cliEnvironment.putEnv(name, value);
    }

    @ShellMethod(key = "env-ls", value = "Lists environment variables")
    public void listEnvironmentVariables() {
        cliEnvironment.getEnvMap().entrySet()
                .stream()
                .map(entry -> entry.getKey()+" = "+entry.getValue())
                .forEach(PrintUtils::printSuccess);
    }
}
