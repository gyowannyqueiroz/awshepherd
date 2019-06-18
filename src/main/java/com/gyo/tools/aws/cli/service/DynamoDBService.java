package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.util.ShellUtils;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamoDBService {

    private DynamoDbClient dynamoDbClient;

    public DynamoDBService() {
        buildDynamoDbClient();
    }

    public void listTables() {
        List<String> tableNames = dynamoDbClient.listTables().tableNames();
        String[][] data = new String[tableNames.size()][1];
        for (int i = 0; i < tableNames.size(); i++) {
            data[i][0] = tableNames.get(i);
        }
        ArrayTableModel model = new ArrayTableModel(data);
        Table table = new TableBuilder(model).addFullBorder(BorderStyle.fancy_light).build();
        ShellUtils.printSuccess(table.render(200));
    }

    private void buildDynamoDbClient() {
        dynamoDbClient = DynamoDbClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.getAwsProfile()))
                .build();
    }

    public void select(String table, String where) {
        ScanResponse response = dynamoDbClient.scan(createRequestScan(table, where));
        printTableHeader(response);
        printTableContent(response);
    }

    private ScanRequest createRequestScan(String table, String where) {
        ScanRequest.Builder reqBuilder = ScanRequest.builder().tableName(table);
        if (where != null && !where.isBlank()) {
            String[] whereSplit = where.split("=");
            reqBuilder.scanFilter(Map.of(whereSplit[0],
                    Condition.builder().comparisonOperator(ComparisonOperator.EQ)
                            .attributeValueList(AttributeValue.builder().s(whereSplit[1]).build()).build()));
        }
        return reqBuilder.build();
    }

    private void printTableContent(ScanResponse response) {
        response.items().forEach(item -> {
            String row = item.keySet().stream().map(key -> item.get(key).s()).collect(Collectors.joining(","));
            ShellUtils.printSuccess(row);
        });
        printTableSummary(response);
    }

    private void printTableSummary(ScanResponse response) {
        printCount(response.count());
    }

    private void printCount(int count) {
        String countString = "Count: " + count;
        ShellUtils.printSuccess("-".repeat(countString.length()));
        ShellUtils.printSuccess(countString);
    }

    private void printTableHeader(ScanResponse response) {
        ShellUtils.printSuccess("COUNT: " + response.count());
        if (response.count() > 0) {
            String columns = response.items().get(0).keySet()
                    .stream().map(String::toUpperCase).collect(Collectors.joining(" | "));
            ShellUtils.printSuccess(columns);
        }
    }

    public void reset() {
        buildDynamoDbClient();
    }
}
