package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.service.CloudWatchLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CloudWatchShell extends AbstractShell {

    @Autowired
    private CloudWatchLogsService cloudWatchLogsService;

    @ShellMethod(key="cwl-ls", value = "Lists all the existing log groups ")
    public void listLogGroups() {
        cloudWatchLogsService.listLogGroups();
    }

    @ShellMethod(key="cwl-logs", value = "Lists the events for the specified log group")
    public void listLogsByGroupName(String logGroupName, @ShellOption(defaultValue = "30") int limit) {
        cloudWatchLogsService.listLogsByGroupName(logGroupName, 30);
    }

    @Override
    void resetServices() {
        cloudWatchLogsService.reset();
    }

}
