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

    @ShellMethod(key="cw-ls", value = "Lists all the existing log groups or the logs of the specified group")
    public void listLog(@ShellOption(defaultValue = "") String logGroupName) {
        cloudWatchLogsService.listLogGroups();
    }

    @Override
    void resetServices() {
        cloudWatchLogsService.reset();
    }

}
