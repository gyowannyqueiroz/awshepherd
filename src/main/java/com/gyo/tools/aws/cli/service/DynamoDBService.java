package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.translator.DynamoDBTableModelTranslator;
import com.gyo.tools.aws.cli.translator.SingleColumnTableModelTranslator;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;

@Service
public class DynamoDBService implements AwsServiceAware {

    private DynamoDbClient dynamoDbClient;

    public DynamoDBService() {
        buildDynamoDbClient();
    }

    public void listTables() {
        List<String> tableNames = dynamoDbClient.listTables().tableNames();
        PrintUtils.printClassicTable(new SingleColumnTableModelTranslator("TABLES", tableNames).translate());
    }

    public void select(String table, String where, int limit) {
        ScanResponse response = dynamoDbClient.scan(createRequestScan(table, where, limit));
        PrintUtils.printClassicTable(new DynamoDBTableModelTranslator(response.items()).translate());
        printTableSummary(response);
    }

    private void buildDynamoDbClient() {
        dynamoDbClient = DynamoDbClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.instance().getAwsProfile()))
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

    private void printTableSummary(ScanResponse response) {
        printCount(response.count());
    }

    private void printCount(int count) {
        String countString = "Count: " + count;
        PrintUtils.printSuccess(countString);
    }

    public void reset() {
        buildDynamoDbClient();
    }
}
