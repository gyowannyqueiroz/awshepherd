package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.util.PrintUtils;
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
        PrintUtils.printTable(data);
    }

    public void select(String table, String where, int limit) {
        ScanResponse response = dynamoDbClient.scan(createRequestScan(table, where, limit));
        List<Map<String, AttributeValue>> items = response.items();
        final int columnCount = items.get(0).keySet().size();
        final String[][] data = new String[items.size()+1][columnCount];
        //column names
        data[0] = items.get(0).keySet().stream().map(String::toUpperCase).collect(Collectors.toList()).toArray(new String[]{});
        if (response.count() > 0) {
            int rowIndex = 1;
            for (Map<String, AttributeValue> item : items) {
                int colIndex = 0;
                for (String col: item.keySet()) {
                    data[rowIndex][colIndex++] = getValue(item.get(col));
                }
                rowIndex++;
            }
        }
        PrintUtils.printTable(data);
        printTableSummary(response);
    }

    private String getValue(AttributeValue colValue) {
        if (colValue != null) {
            if (colValue.s() != null && !colValue.s().isBlank()) {
                return colValue.s();
            }
            if (colValue.n() != null && !colValue.n().isBlank()) {
                return colValue.n();
            }
            if (colValue.bool() != null) {
                return colValue.bool().toString();
            }
            if (colValue.ns() != null) {
                return colValue.l().stream().map(AttributeValue::n).collect(Collectors.joining(","));
            }
            if (colValue.ss() != null) {
                return colValue.l().stream().map(AttributeValue::s).collect(Collectors.joining(","));
            }
        }
        return "";
    }

    private void buildDynamoDbClient() {
        dynamoDbClient = DynamoDbClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.getAwsProfile()))
                .build();
    }

    private ScanRequest createRequestScan(String table, String where, int limit) {
        ScanRequest.Builder reqBuilder = ScanRequest.builder().tableName(table);
        if (where != null && !where.isBlank()) {
            String[] whereSplit = where.split("=");
            reqBuilder.scanFilter(Map.of(whereSplit[0],
                    Condition.builder().comparisonOperator(ComparisonOperator.EQ)
                            .attributeValueList(AttributeValue.builder().s(whereSplit[1]).build()).build()));
        }
        if (limit > -1) {
            reqBuilder.limit(limit);
        }
        return reqBuilder.build();
    }

    private void printTableContent(ScanResponse response) {
        response.items().forEach(item -> {
            String row = item.keySet().stream().map(key -> item.get(key).s()).collect(Collectors.joining(","));
            PrintUtils.printSuccess(row);
        });
        printTableSummary(response);
    }

    private void printTableSummary(ScanResponse response) {
        printCount(response.count());
    }

    private void printCount(int count) {
        String countString = "Count: " + count;
//        PrintUtils.printSuccess("-".repeat(countString.length()));
        PrintUtils.printSuccess(countString);
    }

    private void printTableHeader(ScanResponse response) {
        PrintUtils.printSuccess("COUNT: " + response.count());
        if (response.count() > 0) {
            String columns = response.items().get(0).keySet()
                    .stream().map(String::toUpperCase).collect(Collectors.joining(" | "));
            PrintUtils.printSuccess(columns);
        }
    }

    public void reset() {
        buildDynamoDbClient();
    }
}
