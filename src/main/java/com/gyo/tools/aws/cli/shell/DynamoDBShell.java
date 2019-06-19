package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.service.DynamoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class DynamoDBShell extends AbstractShell {

    @Autowired
    private DynamoDBService dynamoDBService;

    @ShellMethod(key = "db-ls", value = "Lists all DynamoDB tables")
    public void listTables() {
        dynamoDBService.listTables();
    }

    @ShellMethod(key = "db-select", value = "Query DynamoDB table")
    public void select(
            String table, @ShellOption(defaultValue = "") String where,
            @ShellOption(defaultValue="-1") int limit) {
        dynamoDBService.select(table, where, limit);
    }

    @Override
    void resetServices() {
        dynamoDBService.reset();
    }
}
