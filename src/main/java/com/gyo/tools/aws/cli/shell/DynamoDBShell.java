package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEvent;
import com.gyo.tools.aws.cli.service.DynamoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class DynamoDBShell implements ApplicationListener<ProfileChangedEvent> {

    @Autowired
    private DynamoDBService dynamoDBService;

    @ShellMethod(key = "db-tables", value = "Lists all DynamoDB tables")
    public void listTables() {
        dynamoDBService.listTables();
    }

    @ShellMethod(key = "db-select", value = "Query DynamoDB table")
    public void select(String table, @ShellOption(defaultValue = "") String where) {
        dynamoDBService.select(table, where);
    }

    @Override
    public void onApplicationEvent(ProfileChangedEvent event) {
        resetServices();
    }

    private void resetServices() {
        dynamoDBService.reset();
    }
}
