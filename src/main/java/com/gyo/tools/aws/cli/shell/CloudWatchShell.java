package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.service.CloudWatchLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CloudWatchShell extends AbstractShell {

    private CloudWatchLogsService cloudWatchLogsService;

    @Autowired
    public CloudWatchShell(CloudWatchLogsService cloudWatchLogsService, CliEnvironment cliEnvironment) {
        super(cliEnvironment);
        this.cloudWatchLogsService = cloudWatchLogsService;
    }

    @ShellMethod(key="cwl-ls", value = "Lists all the existing log groups or filter by name by using * at the end")
    public void listLogGroups(@ShellOption(defaultValue = "") String filterByName) {
        cloudWatchLogsService.listLogGroups(filterByName);
    }

    @ShellMethod(key="cwl-logs", value = "Lists the events for the specified log group")
    public void listLogsByGroupName(String logGroupName,
                                    @ShellOption(defaultValue = "") String logStream,
                                    @ShellOption(defaultValue = "30") int limit) {
        cloudWatchLogsService.listLogsByGroupName(logGroupName, logStream, limit);
    }

    @Override
    void resetServices() {
        cloudWatchLogsService.reset();
    }

}
